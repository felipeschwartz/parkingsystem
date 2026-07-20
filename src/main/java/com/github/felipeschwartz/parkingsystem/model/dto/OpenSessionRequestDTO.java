package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record OpenSessionRequestDTO(
        @NotNull(message = "Parking space ID cannot be null")
        Long parkingSpaceId,

        // Para sessões avulsas
        @Size(max = 20, message = "License plate must not exceed 20 characters")
        String licensePlate,

        // Para sessões avulsas
        VehicleType vehicleType,

        LocalDateTime entryTime
) {
}