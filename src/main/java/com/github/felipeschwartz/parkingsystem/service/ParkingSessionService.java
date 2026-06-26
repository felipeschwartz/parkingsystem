package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.ParkingSessionMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionDTO;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import com.github.felipeschwartz.parkingsystem.repository.*;
import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSession;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.SpaceStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors; // Importar Collectors

@Service
public class ParkingSessionService {
    private final ParkingSessionRepository sessionRepository;
    private final ParkingSpaceRepository spaceRepository;
    private final VehicleRepository vehicleRepository;
    private final HourlyRateRepository hourlyRateRepository;
    private final SubscriptionContractRepository subscriptionContractRepository;
    private final ParkingSessionMapper sessionMapper; 

    public ParkingSessionService(
            ParkingSessionRepository sessionRepository,
            ParkingSpaceRepository spaceRepository,
            VehicleRepository vehicleRepository,
            HourlyRateRepository hourlyRateRepository,
            SubscriptionContractRepository subscriptionContractRepository,
            ParkingSessionMapper sessionMapper
    ) {
        this.sessionRepository = sessionRepository;
        this.spaceRepository = spaceRepository;
        this.vehicleRepository = vehicleRepository;
        this.hourlyRateRepository = hourlyRateRepository;
        this.subscriptionContractRepository = subscriptionContractRepository;
        this.sessionMapper = sessionMapper; 
    }

    // OPEN - HOURLY
    
    @Transactional
    public ParkingSessionDTO openHourlySession(Long parkingSpaceId, 
                                               String licensePlate,
                                               VehicleType vehicleType,
                                               LocalDateTime entryTime) {

        LocalDateTime when = (entryTime != null) ? entryTime : LocalDateTime.now();

        ParkingSpace space = spaceRepository.findByIdForUpdate(parkingSpaceId)
                .orElseThrow(() -> new ObjectNotFoundException("ParkingSpace", parkingSpaceId));

        validateSpaceCanBeUsed(space, vehicleType);

        if (sessionRepository.existsByParkingSpace_IdAndStatus(space.getId(), SessionStatus.OPEN)) {
            throw new IllegalStateException("There is already an OPEN session for this parking space.");
        }

        ParkingSession session = ParkingSession.forHourly(licensePlate, vehicleType, space, when);

        space.setStatus(SpaceStatus.OCCUPIED);
        spaceRepository.save(space);

        return sessionMapper.toDTO(sessionRepository.save(session)); 
    }

    // OPEN - SUBSCRIPTION
    
    @Transactional
    public ParkingSessionDTO openSubscriptionSession(Long parkingSpaceId, 
                                                     Long vehicleId,
                                                     LocalDateTime entryTime) {

        LocalDateTime when = (entryTime != null) ? entryTime : LocalDateTime.now();

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle", vehicleId));

        ParkingSpace space = spaceRepository.findByIdForUpdate(parkingSpaceId)
                .orElseThrow(() -> new ObjectNotFoundException("ParkingSpace", parkingSpaceId));

        validateSpaceCanBeUsed(space, vehicle.getType());

        if (sessionRepository.existsByParkingSpace_IdAndStatus(space.getId(), SessionStatus.OPEN)) {
            throw new IllegalStateException("There is already an OPEN session for this parking space.");
        }

        boolean hasActiveContract = subscriptionContractRepository.hasActiveContractForDate(
                vehicle.getId(), SubscripionStatus.ACTIVE, when.toLocalDate()
        );
        if (!hasActiveContract) {
            throw new IllegalStateException("Vehicle does not have an active monthly contract for today.");
        }

        ParkingSession session = ParkingSession.forSubscription(vehicle, space, when);

        space.setStatus(SpaceStatus.OCCUPIED);
        spaceRepository.save(space);

        return sessionMapper.toDTO(sessionRepository.save(session)); 
    }

    // CLOSE

    @Transactional
    public ParkingSessionDTO closeSession(Long sessionId, LocalDateTime exitTime) { 
        LocalDateTime when = (exitTime != null) ? exitTime : LocalDateTime.now();

        ParkingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ObjectNotFoundException("ParkingSession", sessionId));

        if (session.getStatus() != SessionStatus.OPEN) {
            throw new IllegalStateException("Only OPEN sessions can be closed.");
        }

        session.close(when);

        BigDecimal amount = calculateAmountFor(session);
        session.setAmountCharged(amount);

        ParkingSpace space = session.getParkingSpace();
        if (space != null) {
            space.setStatus(SpaceStatus.AVAILABLE);
            spaceRepository.save(space);
        }

        return sessionMapper.toDTO(sessionRepository.save(session)); 
    }

    // READ (Novos métodos)

    @Transactional(readOnly = true)
    public ParkingSessionDTO findById(Long sessionId) { 
        return sessionRepository.findById(sessionId)
                .map(sessionMapper::toDTO) 
                .orElseThrow(() -> new ObjectNotFoundException("ParkingSession", sessionId));
    }

    @Transactional(readOnly = true)
    public List<ParkingSessionDTO> findAll() { // Retorna lista de DTOs
        return sessionRepository.findAll().stream()
                .map(sessionMapper::toDTO) 
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParkingSessionDTO> findOpenSessionsByParkingSpace(Long parkingSpaceId) { // Retorna lista de DTOs
        return sessionRepository.findByParkingSpaceIdAndStatus(parkingSpaceId, SessionStatus.OPEN).stream()
                .map(sessionMapper::toDTO) 
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParkingSessionDTO> findSessionsByVehicle(Long vehicleId) { // Retorna lista de DTOs
        return sessionRepository.findByVehicleId(vehicleId).stream()
                .map(sessionMapper::toDTO) 
                .collect(Collectors.toList());
    }

    // HELPERS

    private BigDecimal calculateAmountFor(ParkingSession session) {
        if (session.getVehicle() != null) {
            LocalDate day = session.getEntryTime().toLocalDate();
            boolean hasContract = subscriptionContractRepository.hasActiveContractForDate(
                    session.getVehicle().getId(), SubscripionStatus.ACTIVE, day
            );
            if (hasContract) {
                return BigDecimal.ZERO.setScale(2);
            }
        }

        HourlyRate rate = hourlyRateRepository
                .findByVehicleTypeAndActiveTrue(session.getVehicleType())
                .orElseThrow(() -> new IllegalStateException(
                        "No active HourlyRate found for vehicle type: " + session.getVehicleType()));

        return session.calculateAmount(rate.getRatePerHour());
    }

    private void validateSpaceCanBeUsed(ParkingSpace space, VehicleType vehicleType) {
        if (!Boolean.TRUE.equals(space.getActive())) {
            throw new IllegalStateException("Parking space is inactive.");
        }
        if (space.getStatus() != SpaceStatus.AVAILABLE) {
            throw new IllegalStateException("Parking space is not AVAILABLE.");
        }
        if (space.getVehicleType() != vehicleType) {
            throw new IllegalStateException("Vehicle type not allowed in this parking space.");
        }
    }
}