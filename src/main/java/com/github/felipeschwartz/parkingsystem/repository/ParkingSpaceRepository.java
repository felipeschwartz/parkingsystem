package com.github.felipeschwartz.parkingsystem.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;

import java.util.Optional;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select ps from ParkingSpace ps where ps.id = :id")
    Optional<ParkingSpace> findByIdForUpdate(@Param("id") Long id);
}
