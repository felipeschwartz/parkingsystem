package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import parkingsystem.felipeschwartz.com.github.model.entities.Plan;
import parkingsystem.felipeschwartz.com.github.model.entities.PlanRate;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;

import java.util.Optional;

public interface PlanRateRepository extends JpaRepository<PlanRate, Long> {

    Optional<PlanRate> findByPlanIdAndVehicleType(Long planId, Integer vehicleType);
}
