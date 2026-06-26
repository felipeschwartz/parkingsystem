package com.github.felipeschwartz.parkingsystem.model.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record OpenSubscriptionSessionRequestDTO(
        @NotNull(message = "Parking space ID cannot be null") Long parkingSpaceId,
        @NotNull(message = "Vehicle ID cannot be null") Long vehicleId,
        LocalDateTime entryTime // Opcional, se nulo, o serviço usará LocalDateTime.now()
) {}
