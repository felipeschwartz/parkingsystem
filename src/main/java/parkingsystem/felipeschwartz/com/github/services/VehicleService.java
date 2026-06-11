package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.SubscriptionContract;
import parkingsystem.felipeschwartz.com.github.model.entities.Vehicle;
import parkingsystem.felipeschwartz.com.github.model.enums.SubscripionStatus;
import parkingsystem.felipeschwartz.com.github.repositories.SubscriptionContractRepository;
import parkingsystem.felipeschwartz.com.github.repositories.VehicleRepository;
import parkingsystem.felipeschwartz.com.github.services.exceptions.NoActiveContractException;
import parkingsystem.felipeschwartz.com.github.services.exceptions.VehicleNotFoundException;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final SubscriptionContractRepository contractRepository;

    public VehicleService(VehicleRepository vehicleRepository,
                          SubscriptionContractRepository contractRepository) {
        this.vehicleRepository = vehicleRepository;
        this.contractRepository = contractRepository;
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