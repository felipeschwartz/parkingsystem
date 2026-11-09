package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.SessionStatus;
import com.github.felipeschwartz.parkingsystem.model.enums.VehicleType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParkingSessionDTO(
        Long id,
        VehicleSummaryDTO vehicle,
        String licensePlate,
        VehicleType vehicleType,
        ParkingSpaceDTO parkingSpace,
        PaymentDTO payment,
        LocalDateTime entryTime,
        LocalDateTime exitTime,
        SessionStatus status,
        BigDecimal amountCharged,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}
