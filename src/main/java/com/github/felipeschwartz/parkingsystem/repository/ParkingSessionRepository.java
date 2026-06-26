package com.github.felipeschwartz.parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSession;
import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;

import java.util.List;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {
    boolean existsByParkingSpace_IdAndStatus(Long parkingSpaceId, SessionStatus status);
    List<ParkingSession> findByParkingSpaceIdAndStatus(Long parkingSpaceId, SessionStatus status);
    List<ParkingSession> findByVehicleId(Long vehicleId);

}
