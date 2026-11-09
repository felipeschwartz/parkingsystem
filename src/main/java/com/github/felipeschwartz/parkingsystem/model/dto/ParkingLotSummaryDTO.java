package com.github.felipeschwartz.parkingsystem.model.dto;

public record ParkingLotSummaryDTO(
        Long id,
        String parkingLotName,
        String phoneNumber,
        Integer totalSpaces,
        Boolean active
) {
}
