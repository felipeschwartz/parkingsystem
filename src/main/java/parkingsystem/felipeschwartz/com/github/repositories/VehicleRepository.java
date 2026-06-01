package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import parkingsystem.felipeschwartz.com.github.model.entities.HourlyRate;
import parkingsystem.felipeschwartz.com.github.model.entities.Plan;
import parkingsystem.felipeschwartz.com.github.model.entities.Vehicle;

import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByLicensePlate(String vehicleLicensePlate);
}
