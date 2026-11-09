package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import java.io.Serializable;

public record VehicleSummaryDTO(
        Long id,
        String licensePlate,
        VehicleType type
) implements Serializable {
}
