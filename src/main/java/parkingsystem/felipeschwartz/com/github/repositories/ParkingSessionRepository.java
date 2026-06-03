package parkingsystem.felipeschwartz.com.github.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import parkingsystem.felipeschwartz.com.github.model.entities.ParkingSession;
import parkingsystem.felipeschwartz.com.github.model.enums.SessionStatus;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    boolean existsByParkingSpace_IdAndStatus(Long parkingSpaceId, SessionStatus status);

}
