package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public record CreateUserRequestDTO(
        @NotBlank(message = "Phone cannot be blank") String phone,
        @NotBlank(message = "Email cannot be blank") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Password cannot be blank") @Size(min = 6, message = "Password must be at least 6 characters long") String password,
        @Valid AddressDTO address,
        @NotNull(message = "User type cannot be null") UserType userType,
        @NotNull(message = "User profile cannot be null") UserProfile userProfile,
        String cpf,
        String firstName,
        String lastName,
        LocalDate birthDate,
        String cnpj,
        String billingContact,
        String corporateName,
        String fantasyName,
        @Valid Set<VehicleDTO> vehicles
) {

    public CreateUserRequestDTO {
        vehicles = vehicles == null ? Set.of() : Set.copyOf(vehicles);
    }
}
