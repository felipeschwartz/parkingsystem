package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import parkingsystem.felipeschwartz.com.github.model.entities.HourlyRate;
import parkingsystem.felipeschwartz.com.github.model.entities.Plan;
import parkingsystem.felipeschwartz.com.github.model.entities.Vehicle;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    Optional<Vehicle> findVehicleByLicensePlate(String vehicleLicensePlate);

    boolean existsByLicensePlate(String licensePlate);
}
