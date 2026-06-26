package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record OpenHourlySessionRequestDTO(
        @NotNull(message = "Parking space ID cannot be null") Long parkingSpaceId,
        @NotBlank(message = "License plate cannot be blank") String licensePlate,
        @NotNull(message = "Vehicle type cannot be null") VehicleType vehicleType,
        LocalDateTime entryTime
) {}