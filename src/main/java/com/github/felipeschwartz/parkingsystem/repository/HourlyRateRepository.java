package com.github.felipeschwartz.parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;

import java.util.Optional;

public interface HourlyRateRepository extends JpaRepository<HourlyRate, Long> {

    Optional<HourlyRate> findByVehicleTypeAndActiveTrue(VehicleType vehicleType);
    Optional<HourlyRate> findByVehicleType(Integer vehicleType);

}
