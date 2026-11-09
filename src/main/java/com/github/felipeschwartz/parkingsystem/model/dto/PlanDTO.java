package com.github.felipeschwartz.parkingsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public record PlanDTO(
        Long id,
        @NotBlank @Size(min = 3, max = 100, message = "Plan name must be between 3 and 100 characters") String name,
        Set<SubscriptionContractDTO> subscriptionContracts,
        Set<PlanRateDTO> rates,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {

    public PlanDTO {
        subscriptionContracts = subscriptionContracts == null ? Set.of() : Set.copyOf(subscriptionContracts);
        rates = rates == null ? Set.of() : Set.copyOf(rates);
    }
}
