package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.ReservationMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.ReservationDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import com.github.felipeschwartz.parkingsystem.model.entity.Reservation;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.repository.ParkingSpaceRepository;
import com.github.felipeschwartz.parkingsystem.repository.ReservationRepository;
import com.github.felipeschwartz.parkingsystem.repository.VehicleRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private Logger logger = LoggerFactory.getLogger(PaymentService.class.getName());
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;
    private final VehicleRepository vehicleRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper, VehicleRepository vehicleRepository, ParkingSpaceRepository parkingSpaceRepository) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
        this.vehicleRepository = vehicleRepository;
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    @Transactional(readOnly = true)
    public List<ReservationDTO> findAll() {
        logger.info("Finding all reservations records");
        List<ReservationDTO> reservationDTOS = reservationRepository.findAll().stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
        return reservationDTOS;
    }

    @Transactional
    public ReservationDTO findById(Long id) {
        logger.info("Finding reservation record with id {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->new ObjectNotFoundException("Reservation", id));
        ReservationDTO reservationDTO = reservationMapper.toDTO(reservation);
        return reservationDTO;
    }

    @Transactional
    public ReservationDTO create(ReservationDTO reservationDTO) {
        logger.info("Creating reservation record");
        Vehicle vehicle = vehicleRepository.findById(reservationDTO.vId())
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found with id: " + reservationDTO.vId()));
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(reservationDTO.parkingSpace().id())
                .orElseThrow(() -> new ObjectNotFoundException("ParkingSpace not found with id: " + reservationDTO.parkingSpace().id()));
        Reservation entity = reservationMapper.toEntity(reservationDTO);
        entity.setVehicle(vehicle);
        entity.setParkingSpace(parkingSpace);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        ReservationDTO createdReservationDTO = reservationMapper.toDTO(reservationRepository.save(entity));
        return createdReservationDTO;
    }

    @Transactional
    public ReservationDTO update(ReservationDTO updated) {
        logger.info("Updating reservation record with Id {}", updated.id());
        Reservation existingReservation = reservationRepository.findById(updated.id())
                .orElseThrow(() ->new ObjectNotFoundException("Reservation", updated.id()));
        if (updated.vId() != null) {
            Vehicle newVehice = reservationRepository.findById(updated.vId())
                    .orElseThrow(() -> new ObjectNotFoundException("Reservation not found with id: " + updated.vId())).getVehicle();
            existingReservation.setVehicle(newVehice);
        }
        reservationMapper.updateReservationFromDto(updated, existingReservation);
        existingReservation.setUpdatedAt(LocalDateTime.now());
        ReservationDTO updatedReservationDTO = reservationMapper.toDTO(reservationRepository.save(existingReservation));
        return updatedReservationDTO;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting reservation record with id {}", id);
        if (!reservationRepository.existsById(id)) {
            throw new ObjectNotFoundException("Reservation not found with id: " + id);
        }
        reservationRepository.deleteById(id);
    }

}
