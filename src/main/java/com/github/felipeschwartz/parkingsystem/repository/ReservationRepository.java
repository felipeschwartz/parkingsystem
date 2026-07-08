package com.github.felipeschwartz.parkingsystem.repository;

import com.github.felipeschwartz.parkingsystem.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
