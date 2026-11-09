package com.github.felipeschwartz.parkingsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.felipeschwartz.parkingsystem.model.enums.SubscripionStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SubscriptionContractDTO(
        Long id,
        VehicleSummaryDTO vehicle,
        @JsonIgnore PlanSummaryDTO plan,
        Long pId,
        LocalDate startDate,
        LocalDate endDate,
        SubscripionStatus status,
        UserSummaryDTO user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {

    public SubscriptionContractDTO {
        status = status == null ? SubscripionStatus.ACTIVE : status;
    }
}
