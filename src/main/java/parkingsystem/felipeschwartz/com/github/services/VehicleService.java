package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.*;
import parkingsystem.felipeschwartz.com.github.model.enums.SubscripionStatus;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
import parkingsystem.felipeschwartz.com.github.repositories.SubscriptionContractRepository;
import parkingsystem.felipeschwartz.com.github.repositories.VehicleRepository;
import parkingsystem.felipeschwartz.com.github.services.exceptions.NoActiveContractException;
import parkingsystem.felipeschwartz.com.github.services.exceptions.VehicleNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SubscriptionContractRepository contractRepository;

    public VehicleService(VehicleRepository vehicleRepository,
                          SubscriptionContractRepository contractRepository) {
        this.vehicleRepository = vehicleRepository;
        this.contractRepository = contractRepository;
    }

    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + id));
    }


    @Transactional
    public Vehicle create(Vehicle vehicle) {
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

        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public Vehicle update(Long id, Vehicle updated) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found: " + id));

        if (updated.getLicensePlate() != null) vehicle.setLicensePlate(updated.getLicensePlate());
        if (updated.getType() != null) vehicle.setType(updated.getType());
        if (updated.getOwner() != null) vehicle.setOwner(updated.getOwner());

        vehicle.setUpdatedAt(LocalDateTime.now());
        return vehicleRepository.save(vehicle);
    }

    @Transactional
    public void delete(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found: " + id));
        vehicleRepository.delete(vehicle);
    }

    /**
     * Busca um veículo pela placa.
     * Lança VehicleNotFoundException se não encontrado.
     */
    @Transactional(readOnly = true)
    public Vehicle findByLicensePlate(String licensePlate) {
        return vehicleRepository
                .findVehicleByLicensePlate(licensePlate)
                .orElseThrow(() -> new VehicleNotFoundException(licensePlate));
        //      ^ Desembrulha o Optional — se vazio, lança a exceção
    }

    /**
     * Verifica se o veículo (pela placa) possui contrato ativo.
     * Retorna true se tiver, false se não tiver.
     */
    @Transactional(readOnly = true)
    public boolean hasActiveContract(String licensePlate) {
        Vehicle vehicle = findByLicensePlate(licensePlate); // já é Vehicle, não Optional
        return contractRepository
                .findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE)
                .isPresent();
    }

    /**
     * Busca o contrato ativo do veículo pela placa.
     * Lança NoActiveContractException se não houver contrato ativo.
     */
    @Transactional(readOnly = true)
    public SubscriptionContract findActiveContract(String licensePlate) {
        Vehicle vehicle = findByLicensePlate(licensePlate); // já é Vehicle, não Optional
        return contractRepository
                .findByVehicleAndStatus(vehicle, SubscripionStatus.ACTIVE)
                .orElseThrow(() -> new NoActiveContractException(licensePlate));
    }



}