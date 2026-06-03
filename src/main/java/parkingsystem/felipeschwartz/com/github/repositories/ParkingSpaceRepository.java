package parkingsystem.felipeschwartz.com.github.repositories;

import jakarta.persistence.LockModeType;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import parkingsystem.felipeschwartz.com.github.model.entities.ParkingSpace;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;

import java.util.Optional;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ps from ParkingSpace ps where ps.id = :id")
    Optional<ParkingSpace> findByIdForUpdate(@Param("id") Long id);
}
