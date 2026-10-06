package com.github.felipeschwartz.parkingsystem.model.dto;

public record LoginResponseDTO(
        String token,
        UserSummaryDTO user
) {}
