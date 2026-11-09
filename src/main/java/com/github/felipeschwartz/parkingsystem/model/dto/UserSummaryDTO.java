package com.github.felipeschwartz.parkingsystem.model.dto;

import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import java.util.Set;

public record UserSummaryDTO(
        Long id,
        String name,
        String phone,
        String email,
        UserType userType,
        String cpf,
        String cnpj,
        Set<String> roles
) {
}
