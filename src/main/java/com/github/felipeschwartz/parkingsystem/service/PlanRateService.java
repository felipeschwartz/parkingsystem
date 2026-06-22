package com.github.felipeschwartz.parkingsystem.service;

import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import com.github.felipeschwartz.parkingsystem.repository.PlanRateRepository;

@Service
public class PlanRateService  {

    private final PlanRateRepository planRateRepository;

    public PlanRateService(PlanRateRepository planRateRepository) {
        this.planRateRepository = planRateRepository;
    }

    public PlanRate findByPlanIdAndVehicleType(Long planId, VehicleType vehicleType) {
        return planRateRepository
                .findByPlanIdAndVehicleType(planId, vehicleType)
                .orElseThrow(() -> new RuntimeException(
                        "PlanRate not found for planId=" + planId + " and vehicleType=" + vehicleType
                ));
    }


}
