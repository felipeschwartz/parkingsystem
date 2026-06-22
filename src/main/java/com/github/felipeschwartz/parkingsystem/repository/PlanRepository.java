package com.github.felipeschwartz.parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;

import java.util.Optional;
@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    Optional<Plan> findPlanById(Long id);
}
