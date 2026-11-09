package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public record UserIndividualDTO(
        Long id,
        String phone,
        String email,
        Set<VehicleDTO> vehicles,
        AddressDTO address,
        UserType userType,
        UserProfile userProfile,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String cpf,
        String firstName,
        String lastName,
        LocalDate birthDate
) implements UserDTO {

    public UserIndividualDTO {
        vehicles = vehicles == null ? Set.of() : Set.copyOf(vehicles);
    }
}
