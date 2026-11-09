package com.github.felipeschwartz.parkingsystem.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public record ParkingLotDTO(
        Long id,
        String parkingLotName,
        AddressDTO address,
        String phoneNumber,
        Integer totalSpaces,
        Boolean active,
        Set<ParkingSpaceDTO> parkingSpaces,
        Integer carSpaces,
        Integer motorcycleSpaces,
        Integer truckSpaces,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {

    public ParkingLotDTO {
        parkingSpaces = parkingSpaces == null ? Set.of() : Set.copyOf(parkingSpaces);
    }
}
