package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import java.time.LocalDateTime;
import java.util.Set;

public record UserEntityDTO(
        Long id,
        String phone,
        String email,
        Set<VehicleDTO> vehicles,
        AddressDTO address,
        UserType userType,
        UserProfile userProfile,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String cnpj,
        String billingContact,
        String corporateName,
        String fantasyName
) implements UserDTO {

    public UserEntityDTO {
        vehicles = vehicles == null ? Set.of() : Set.copyOf(vehicles);
    }
}
