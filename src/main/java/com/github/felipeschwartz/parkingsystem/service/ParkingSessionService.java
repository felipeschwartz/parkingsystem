package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.controller.ParkingLotController;
import com.github.felipeschwartz.parkingsystem.controller.ParkingSessionController;
import com.github.felipeschwartz.parkingsystem.mapper.ParkingSessionMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.OpenSessionRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingLotDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSession;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.SpaceStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.repository.*;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class ParkingSessionService {
    private Logger logger = LoggerFactory.getLogger(ParkingLotService.class.getName());

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

    //OPEN
    @Transactional
    public ParkingSessionDTO openParkingSession(OpenSessionRequestDTO request) {
        logger.info("Opening Parking Session");

        LocalDateTime entryTime = (request.entryTime() != null) ? request.entryTime() : LocalDateTime.now();
        ParkingSpace space = spaceRepository.findById(request.parkingSpaceId())
                .orElseThrow(() -> new ObjectNotFoundException("ParkingSpace not found with ID: " + request.parkingSpaceId()));
        if (sessionRepository.existsByParkingSpace_IdAndStatus(space.getId(), SessionStatus.OPEN)) {
            throw new ObjectNotFoundException("There is already an OPEN session for parking space ID: " + space.getId());
        }
        Optional<Vehicle> existingVehicle = vehicleRepository.findVehicleByLicensePlate(request.licensePlate());

        ParkingSession session;
        Vehicle vehicle = null;

        if  (existingVehicle.isPresent()) {
            vehicle = existingVehicle.get();
            boolean hasActiveContract = subscriptionContractRepository.existsByVehicleIdAndStatusAndEndDateGreaterThanEqual(
                    vehicle.getId(), SubscripionStatus.ACTIVE, entryTime.toLocalDate());
            if (hasActiveContract) {
                session = ParkingSession.forSubscription(vehicle, space, entryTime);
            } else {
                session = ParkingSession.forHourly(request.licensePlate(), request.vehicleType(), space, entryTime);
            }
        } else {
            session = ParkingSession.forHourly(request.licensePlate(), request.vehicleType(), space, entryTime);
        }

        space.setStatus(SpaceStatus.OCCUPIED);
        spaceRepository.save(space);
        ParkingSession savedSession = sessionRepository.save(session);
        ParkingSessionDTO createdSessionDTO = sessionMapper.toDTO(savedSession);
        addHateoasLinks(createdSessionDTO);
        return createdSessionDTO;
    }


    // CLOSE

    @Transactional
    public ParkingSessionDTO closeSession(Long sessionId, LocalDateTime exitTime) { 
        LocalDateTime actualExitTime = (exitTime != null) ? exitTime : LocalDateTime.now();

        ParkingSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ObjectNotFoundException("ParkingSession", sessionId));

        if (session.getStatus() != SessionStatus.OPEN) {
            throw new IllegalStateException("Only OPEN sessions can be closed.");
        }

        session.close(actualExitTime);

        BigDecimal amount = calculateAmountFor(session);
        session.setAmountCharged(amount);

        ParkingSpace space = session.getParkingSpace();
        if (space != null) {
            space.setStatus(SpaceStatus.AVAILABLE);
            spaceRepository.save(space);
        }
        ParkingSession savedSession = sessionRepository.save(session);
        ParkingSessionDTO updatedSessionDTO = sessionMapper.toDTO(savedSession);
        addHateoasLinks(updatedSessionDTO);
        return updatedSessionDTO;
    }

    // READ (Novos métodos)

    @Transactional(readOnly = true)
    public ParkingSessionDTO findById(Long id) {
        logger.info("Finding Parking Session with ID {}", id);
        ParkingSession parkingSession = sessionRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Parking Session not found: ", id));
        ParkingSessionDTO parkingSessionDTO = sessionMapper.toDTO(parkingSession);
        addHateoasLinks(parkingSessionDTO);
        return parkingSessionDTO;
    }

    @Transactional(readOnly = true)
    public List<ParkingSessionDTO> findAll() {
        logger.info("Finding all Parking Sessions");
        List<ParkingSessionDTO> parkingSessionDTOS = sessionRepository.findAll().stream()
                .map(sessionMapper::toDTO)
                .collect(Collectors.toList());
        parkingSessionDTOS.forEach(this::addHateoasLinks);
        return parkingSessionDTOS;
    }

    @Transactional(readOnly = true)
    public List<ParkingSessionDTO> findOpenSessionsByParkingSpace(Long parkingSpaceId) {
        return sessionRepository.findByParkingSpaceIdAndStatus(parkingSpaceId, SessionStatus.OPEN).stream()
                .map(sessionMapper::toDTO) 
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ParkingSessionDTO> findSessionsByVehicle(Long vehicleId) {
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

    private void addHateoasLinks(ParkingSessionDTO dto) {
        dto.add(linkTo(methodOn(ParkingSessionController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(ParkingSessionController.class).findAll()).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(ParkingSessionController.class).openSession(null)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(ParkingSessionController.class).closeSession(dto.getId(), null)).withRel("close").withType("PUT"));
    }
}