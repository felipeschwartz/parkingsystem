package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public record VehicleDTO(
        Long id,
        String licensePlate,
        VehicleType type,
        UserSummaryDTO user,
        Set<SubscriptionContractDTO> subscriptionContracts,
        Set<ParkingSessionDTO> sessions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {

    public VehicleDTO {
        subscriptionContracts = subscriptionContracts == null ? Set.of() : Set.copyOf(subscriptionContracts);
        sessions = sessions == null ? Set.of() : Set.copyOf(sessions);
    }
}
