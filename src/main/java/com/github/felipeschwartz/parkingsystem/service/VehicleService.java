package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.mapper.VehicleMapper;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import com.github.felipeschwartz.parkingsystem.repository.SubscriptionContractRepository;
import com.github.felipeschwartz.parkingsystem.repository.VehicleRepository;
import com.github.felipeschwartz.parkingsystem.service.exceptions.NoActiveContractException;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
        return vehicleRepository.findAll().stream().map(vehicleMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public VehicleDTO findById(Long id) {
        logger.info("Finding one Vehicle!");
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found: ", id));
        return vehicleMapper.toDTO(vehicle);

    }

    // -------- CREATE --------


    @Transactional
    public VehicleDTO create(Vehicle vehicle) {
        logger.info("Creating one Vehicle!");
        Objects.requireNonNull(vehicle, "vehicle must not be null.");

        String licencePlate = vehicle.getLicensePlate();
        if (licencePlate == null || licencePlate.isBlank()) {
            throw new IllegalArgumentException("Licence Plate must not be blank.");
        }
        if (vehicleRepository.existsByLicensePlate(licencePlate)) {
            throw new IllegalStateException("An Vehicle with this Licence Plate already exists.");
        }

        LocalDateTime now = LocalDateTime.now();
        vehicle.setCreatedAt(now);
        vehicle.setUpdatedAt(now);

        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    // -------- UPDATE --------

    @Transactional
    public VehicleDTO update(Long id, Vehicle updated) {
        logger.info("Updating one Vehicle!");
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found: ", id));

        if (updated.getLicensePlate() != null) vehicle.setLicensePlate(updated.getLicensePlate());
        if (updated.getType() != null) vehicle.setType(updated.getType());
        if (updated.getOwner() != null) vehicle.setOwner(updated.getOwner());

        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    // -------- DELETE --------

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting one Vehicle!");
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle not found: ", id));
        vehicleRepository.delete(vehicle);
    }

    /*
     * Busca um veículo pela placa.
     * Lança VehicleNotFoundException se não encontrado.
     */
    @Transactional(readOnly = true)
    public VehicleDTO findByLicensePlate(String licensePlate) {
        logger.info("Finding one Vehicle by Licence Plate!");
        Vehicle vehicle = vehicleRepository.findVehicleByLicensePlate(licensePlate)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle ", licensePlate));

        return vehicleMapper.toDTO(vehicle);
    }


    //    Método nâo exposto via API

    private Vehicle findVehicleEntityByLicensePlate(String licensePlate) {
        return vehicleRepository.findVehicleByLicensePlate(licensePlate)
                .orElseThrow(() -> new ObjectNotFoundException("Vehicle ", licensePlate));
    }

    /*
     * Verifica se o veículo (pela placa) possui contrato ativo.
     * Retorna true se tiver, false se não tiver.
     */
    @Transactional(readOnly = true)
    public boolean hasActiveContract(String licensePlate) {
        logger.info("Verifying if a Vehicle has an active Contract!");
        Vehicle vehicle = findVehicleEntityByLicensePlate(licensePlate);
        return contractRepository
                .findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE)
                .isPresent();
    }

    /*
     * Busca o contrato ativo do veículo pela placa.
     * Lança NoActiveContractException se não houver contrato ativo.
     */
    @Transactional(readOnly = true)
    public SubscriptionContract findActiveContract(String licensePlate) {
        logger.info("Finding an active Contract!");
        Vehicle vehicle = findVehicleEntityByLicensePlate(licensePlate); // já é Vehicle, não Optional
        return contractRepository
                .findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE)
                .orElseThrow(() -> new NoActiveContractException(licensePlate));
    }



}