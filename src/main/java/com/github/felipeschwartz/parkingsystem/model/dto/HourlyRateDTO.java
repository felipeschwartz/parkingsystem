package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record HourlyRateDTO(
        Long id,
        VehicleType vehicleType,
        BigDecimal ratePerHour,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}
