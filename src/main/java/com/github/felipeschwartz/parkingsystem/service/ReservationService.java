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
        return reservationRepository.findAll().stream().map(reservation -> reservationMapper.toDTO(reservation))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReservationDTO findById(Long id) {
        logger.info("Finding reservation record with id {}", id);
        Reservation rate = reservationRepository.findById(id).orElseThrow(() ->new ObjectNotFoundException("Reservation not found with id: ", id));
        return reservationMapper.toDTO(rate);
    }


    @Transactional
    public ReservationDTO create(ReservationDTO reservationDTO) {
        logger.info("Creating reservation record {}", reservationDTO);
        Vehicle vehicle = vehicleRepository.findById(reservationDTO.getvId())
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found with id: " + reservationDTO.getvId()));
        ParkingSpace parkingSpace = parkingSpaceRepository.findById(reservationDTO.getParkingSpace().getId())
                .orElseThrow(() -> new ObjectNotFoundException("ParkingSpace not found with id: " + reservationDTO.getParkingSpace().getId()));
        Reservation entity = reservationMapper.toEntity(reservationDTO);
        entity.setVehicle(vehicle);
        entity.setParkingSpace(parkingSpace);
        entity = reservationRepository.save(entity);
        return reservationMapper.toDTO(entity);
    }


    @Transactional
    public ReservationDTO update(ReservationDTO updated) {
        logger.info("Updating reservation record {}", updated);
        Reservation existingReservation = reservationRepository.findById(updated.getId())
                .orElseThrow(() ->new ObjectNotFoundException("Reservation not found with id: ", updated.getId()));
        if (updated.getvId() != null) {
            Vehicle newVehice = reservationRepository.findById(updated.getvId())
                    .orElseThrow(() -> new ObjectNotFoundException("Reservation not found with id: " + updated.getvId())).getVehicle();
            existingReservation.setVehicle(newVehice);
        }
        reservationMapper.updateReservationFromDto(updated, existingReservation);
        existingReservation.setUpdatedAt(LocalDateTime.now());
        Reservation updatedReservation = reservationRepository.save(existingReservation);
        return reservationMapper.toDTO(updatedReservation);
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting reservation record with id {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found: " + id));
        reservationRepository.delete(reservation);
    }
}
