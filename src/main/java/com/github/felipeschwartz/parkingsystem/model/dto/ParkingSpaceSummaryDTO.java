package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.SpaceStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import java.io.Serializable;

public record ParkingSpaceSummaryDTO(
        Long id,
        String floor,
        String position,
        VehicleType vehicleType,
        SpaceStatus status
) implements Serializable {
}
