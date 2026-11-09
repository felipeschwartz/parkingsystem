package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.SpaceStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import java.io.Serializable;
import java.time.LocalDateTime;

public record ParkingSpaceDTO(
        Long id,
        String floor,
        String position,
        VehicleType vehicleType,
        SpaceStatus status,
        Boolean active,
        ParkingLotSummaryDTO parkingLot,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}
