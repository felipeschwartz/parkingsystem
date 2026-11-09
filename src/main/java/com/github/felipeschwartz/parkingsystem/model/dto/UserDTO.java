package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

public sealed interface UserDTO extends Serializable permits UserIndividualDTO, UserEntityDTO {

    Long id();

    String phone();

    String email();

    Set<VehicleDTO> vehicles();

    AddressDTO address();

    UserType userType();

    UserProfile userProfile();

    LocalDateTime createdAt();

    LocalDateTime updatedAt();
}
