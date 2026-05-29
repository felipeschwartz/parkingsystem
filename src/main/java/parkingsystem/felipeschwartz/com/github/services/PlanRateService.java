package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.PlanRate;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
import parkingsystem.felipeschwartz.com.github.repositories.PlanRateRepository;

@Service
public class PlanRateService  {

    private final PlanRateRepository planRateRepository;

    public PlanRateService(PlanRateRepository planRateRepository) {
        this.planRateRepository = planRateRepository;
    }

    public PlanRate findByPlanIdAndVehicleType(Long planId, VehicleType vehicleType) {
        return planRateRepository
                .findByPlanIdAndVehicleType(planId, vehicleType.getCode())
                .orElseThrow(() -> new RuntimeException(
                        "PlanRate not found for planId=" + planId + " and vehicleType=" + vehicleType
                ));
    }


}
