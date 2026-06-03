package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import parkingsystem.felipeschwartz.com.github.model.entities.Plan;
import parkingsystem.felipeschwartz.com.github.model.entities.PlanRate;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;

import java.util.Optional;

public interface PlanRateRepository extends JpaRepository<PlanRate, Long> {

    boolean existsByPlan_IdAndVehicleTypeAndDurationMonths(Long planId, VehicleType vehicleType, Integer durationMonths);

    Optional<PlanRate> findByPlanIdAndVehicleType(Long planId, VehicleType vehicleType);
}
