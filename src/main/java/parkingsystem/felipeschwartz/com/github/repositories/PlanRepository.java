package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import parkingsystem.felipeschwartz.com.github.model.entities.Plan;

public interface PlanRepository extends JpaRepository<Plan, Long> {}
