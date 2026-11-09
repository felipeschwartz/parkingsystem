package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.mapper.VehicleMapper;
import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import com.github.felipeschwartz.parkingsystem.repository.SubscriptionContractRepository;
import com.github.felipeschwartz.parkingsystem.repository.VehicleRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.NoActiveContractException;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    private Logger logger = LoggerFactory.getLogger(VehicleService.class.getName());

    private final VehicleRepository vehicleRepository;
    private final SubscriptionContractRepository contractRepository;
    private final VehicleMapper vehicleMapper;

    public VehicleService(VehicleRepository vehicleRepository,
                          SubscriptionContractRepository contractRepository, VehicleMapper vehicleMapper) {
        this.vehicleRepository = vehicleRepository;
        this.contractRepository = contractRepository;
        this.vehicleMapper = vehicleMapper;
    }

    @Transactional(readOnly = true)
    public List<VehicleDTO> findAll() {
        logger.info("Finding all Vehicles!");
        List<VehicleDTO> vehicles = vehicleRepository.findAll().stream()
                .map(vehicleMapper::toDTO)
                .collect(Collectors.toList());
        return vehicles;
    }

    @Transactional(readOnly = true)
    public VehicleDTO findById(Long id) {
        logger.info("Finding one Vehicle!");
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle", id));
        VehicleDTO dto = vehicleMapper.toDTO(vehicle);
        return dto;

    }

    @Transactional
    public VehicleDTO create(VehicleDTO vehicleDTO) {
        logger.info("Creating one Vehicle!");
        Objects.requireNonNull(vehicleDTO, "vehicle must not be null.");

        String licencePlate = vehicleDTO.licensePlate();
        if (licencePlate == null || licencePlate.isBlank()) {
            throw new IllegalArgumentException("Licence Plate must not be blank.");
        }
        if (vehicleRepository.existsByLicensePlate(licencePlate)) {
            throw new IllegalStateException("An Vehicle with this Licence Plate already exists.");
        }
        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        LocalDateTime now = LocalDateTime.now();
        vehicle.setCreatedAt(now);
        vehicle.setUpdatedAt(now);
        VehicleDTO createdDto = vehicleMapper.toDTO(vehicleRepository.save(vehicle));
        return createdDto;
    }

    @Transactional
    public VehicleDTO update(VehicleDTO updated) {
        logger.info("Updating one Vehicle!");
        Vehicle vehicle = vehicleRepository.findById(updated.id())
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle", updated.id()));

        if (updated.licensePlate() != null) vehicle.setLicensePlate(updated.licensePlate());
        if (updated.type() != null) vehicle.setType(updated.type());

        vehicle.setUpdatedAt(LocalDateTime.now());
        VehicleDTO updatedDto = vehicleMapper.toDTO(vehicleRepository.save(vehicle));
        return updatedDto;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting one Vehicle!");
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle", id));
        vehicleRepository.delete(vehicle);
    }

    @Transactional(readOnly = true)
    public VehicleDTO findByLicensePlate(String licensePlate) {
        logger.info("Finding one Vehicle by Licence Plate!");
        Vehicle vehicle = vehicleRepository.findVehicleByLicensePlate(licensePlate)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle", licensePlate));
        VehicleDTO dto = vehicleMapper.toDTO(vehicle);
        return dto;
    }

    private Vehicle findVehicleEntityByLicensePlate(String licensePlate) {
        return vehicleRepository.findVehicleByLicensePlate(licensePlate)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle", licensePlate));
    }
    @Transactional(readOnly = true)
    public boolean hasActiveContract(String licensePlate) {
        logger.info("Verifying if a Vehicle has an active Contract!");
        Vehicle vehicle = findVehicleEntityByLicensePlate(licensePlate);
        return contractRepository
                .findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE)
                .isPresent();
    }

    @Transactional(readOnly = true)
    public SubscriptionContract findActiveContract(String licensePlate) {
        logger.info("Finding an active Contract!");
        Vehicle vehicle = findVehicleEntityByLicensePlate(licensePlate);
        return contractRepository
                .findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE)
                .orElseThrow(() -> new NoActiveContractException(licensePlate));
    }

}