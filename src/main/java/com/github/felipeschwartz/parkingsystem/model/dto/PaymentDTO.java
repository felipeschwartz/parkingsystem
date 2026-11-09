package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.PaymentMethod;
import com.github.felipeschwartz.parkingsystem.model.enums.PaymentStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDTO(
        Long id,
        ParkingSessionSummaryDTO parkingSession,
        BigDecimal amount,
        LocalDateTime paymentDate,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        String reference,
        LocalDateTime createdDate,
        LocalDateTime updatedDate
) implements Serializable {
}
