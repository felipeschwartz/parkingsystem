package com.github.felipeschwartz.parkingsystem.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public record PlanRateDTO(
        Long id,
        @JsonIgnore PlanSummaryDTO plan,
        Long pId,
        BigDecimal discountPercent,
        VehicleType vehicleType,
        Integer durationMonths,
        BigDecimal monthlyPrice,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {

    public BigDecimal calculatePrice() {
        if (monthlyPrice == null) {
            throw new IllegalStateException("monthlyPrice não pode ser nulo");
        }

        BigDecimal discount = (discountPercent == null) ? BigDecimal.ZERO : discountPercent;
        BigDecimal discountFactor = discount.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        return monthlyPrice
                .subtract(monthlyPrice.multiply(discountFactor))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
