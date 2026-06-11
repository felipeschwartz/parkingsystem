package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parkingsystem.felipeschwartz.com.github.model.entities.Plan;

import java.util.Optional;
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findPlanById(Long id);
}
