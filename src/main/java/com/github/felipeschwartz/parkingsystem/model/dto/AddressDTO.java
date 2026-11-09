package com.github.felipeschwartz.parkingsystem.model.dto;

public record AddressDTO(
        String street,
        String streetNumber,
        String complement,
        String city,
        String state,
        String zip,
        String country
) {
}
