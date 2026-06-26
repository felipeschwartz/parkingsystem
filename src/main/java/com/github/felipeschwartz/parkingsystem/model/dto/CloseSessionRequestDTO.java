package com.github.felipeschwartz.parkingsystem.model.dto;

import java.time.LocalDateTime;

public record CloseSessionRequestDTO(
        LocalDateTime exitTime
) {}