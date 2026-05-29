package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.HourlyRate;
import parkingsystem.felipeschwartz.com.github.model.entities.Vehicle;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
import parkingsystem.felipeschwartz.com.github.repositories.HourlyRateRepository;
import parkingsystem.felipeschwartz.com.github.repositories.VehicleRepository;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle findByLicensePlate(Vehicle vehicle) {
        return vehicleRepository.findByLicensePlate(vehicle.getLicensePlate())
                .orElseThrow(() -> new RuntimeException(
                        "Vehicle not found for this Licence Plate=" + vehicle.getLicensePlate()
                ));
    }


}
