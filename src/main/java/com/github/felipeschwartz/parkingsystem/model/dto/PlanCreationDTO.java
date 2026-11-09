package com.github.felipeschwartz.parkingsystem.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record PlanCreationDTO(
        @NotBlank(message = "Plan name cannot be null or empty") @Size(min = 3, max = 100, message = "Plan name must be between 3 and 100 characters") String name,
        @NotNull(message = "Active status cannot be null") Boolean active,
        List<Long> rateIds,
        List<Long> subscriptionContractIds
) {
}
