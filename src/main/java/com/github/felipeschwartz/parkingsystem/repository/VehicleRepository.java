package com.github.felipeschwartz.parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    Optional<Vehicle> findVehicleByLicensePlate(String vehicleLicensePlate);

    boolean existsByLicensePlate(String licensePlate);
}
