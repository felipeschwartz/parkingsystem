package com.github.felipeschwartz.parkingsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.felipeschwartz.parkingsystem.model.enums.ReservationStatus;
import java.io.Serializable;
import java.time.LocalDateTime;

public record ReservationDTO(
        Long id,
        @JsonIgnore VehicleSummaryDTO vehicle,
        Long vId,
        ParkingSpaceDTO parkingSpace,
        LocalDateTime startTime,
        LocalDateTime endTime,
        ReservationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}
