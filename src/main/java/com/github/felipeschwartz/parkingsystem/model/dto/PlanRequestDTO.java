package com.github.felipeschwartz.parkingsystem.model.dto;

import java.util.List;

public record PlanRequestDTO(
        String name,
        Boolean active,
        List<Long> rateIds,
        List<Long> subscriptionContractIds
) {
}
