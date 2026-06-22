package com.github.felipeschwartz.parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;

import java.util.Optional;

public interface PlanRateRepository extends JpaRepository<PlanRate, Long> {

    boolean existsByPlan_IdAndVehicleTypeAndDurationMonths(Long planId, VehicleType vehicleType, Integer durationMonths);

    Optional<PlanRate> findByPlanIdAndVehicleType(Long planId, VehicleType vehicleType);
}
