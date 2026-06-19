package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.data.dto.VehicleDTO;
import parkingsystem.felipeschwartz.com.github.mapper.OwnerIndividualMapper;
import parkingsystem.felipeschwartz.com.github.mapper.VehicleMapper;
import parkingsystem.felipeschwartz.com.github.model.entities.*;
import parkingsystem.felipeschwartz.com.github.model.enums.SubscripionStatus;
import parkingsystem.felipeschwartz.com.github.repositories.SubscriptionContractRepository;
import parkingsystem.felipeschwartz.com.github.repositories.VehicleRepository;
import parkingsystem.felipeschwartz.com.github.services.exceptions.NoActiveContractException;
import parkingsystem.felipeschwartz.com.github.services.exceptions.ObjectNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SubscriptionContractRepository contractRepository;

    public VehicleService(VehicleRepository vehicleRepository,
                          SubscriptionContractRepository contractRepository) {
        this.vehicleRepository = vehicleRepository;
        this.contractRepository = contractRepository;
    }

    @Autowired
    private VehicleMapper vehicleMapper;

    @Transactional(readOnly = true)
    public List<VehicleDTO> findAll() {

        return vehicleRepository.findAll().stream().map(vehicleMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public VehicleDTO findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + id));
        return vehicleMapper.toDTO(vehicle);

    }

    // -------- CREATE --------


    @Transactional
    public VehicleDTO create(Vehicle vehicle) {
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
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + id));

        if (updated.getLicensePlate() != null) vehicle.setLicensePlate(updated.getLicensePlate());
        if (updated.getType() != null) vehicle.setType(updated.getType());
        if (updated.getOwner() != null) vehicle.setOwner(updated.getOwner());

        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleMapper.toDTO(vehicleRepository.save(vehicle));
    }

    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + id));
        vehicleRepository.delete(vehicle);
    }

    /*
     * Busca um veículo pela placa.
     * Lança VehicleNotFoundException se não encontrado.
     */
    @Transactional(readOnly = true)
    public VehicleDTO findByLicensePlate(String licensePlate) {
        Vehicle vehicle = vehicleRepository.findVehicleByLicensePlate(licensePlate)
                .orElseThrow(() -> new ObjectNotFoundException(licensePlate));

        return vehicleMapper.toDTO(vehicle);
    }


    //    Método nâo exposto via API

    private Vehicle findVehicleEntityByLicensePlate(String licensePlate) {
        return vehicleRepository.findVehicleByLicensePlate(licensePlate)
                .orElseThrow(() -> new ObjectNotFoundException(licensePlate));
    }

    /*
     * Verifica se o veículo (pela placa) possui contrato ativo.
     * Retorna true se tiver, false se não tiver.
     */
    @Transactional(readOnly = true)
    public boolean hasActiveContract(String licensePlate) {
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
        Vehicle vehicle = findVehicleEntityByLicensePlate(licensePlate); // já é Vehicle, não Optional
        return contractRepository
                .findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE)
                .orElseThrow(() -> new NoActiveContractException(licensePlate));
    }



}