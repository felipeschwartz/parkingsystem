package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.HourlyRate;
import parkingsystem.felipeschwartz.com.github.model.entities.PlanRate;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
import parkingsystem.felipeschwartz.com.github.repositories.HourlyRateRepository;
import parkingsystem.felipeschwartz.com.github.repositories.PlanRateRepository;

@Service
public class HourlyRateService {

    private final HourlyRateRepository hourlyRateRepository;

    public HourlyRateService(HourlyRateRepository hourlyRateRepository) {
        this.hourlyRateRepository = hourlyRateRepository;
    }

    public HourlyRate findByVehicleType(VehicleType vehicleType) {
        return hourlyRateRepository
                .findByVehicleType(vehicleType.getCode())
                .orElseThrow(() -> new RuntimeException(
                        "HourlyRate not found for Vehicle Type=" + vehicleType
                ));
    }


}
