package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.UserSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserSummaryMapper {

    @Named("userToSummaryDTO")
    @Mapping(target = "cpf", ignore = true) // Ignorar para que seja preenchido no @AfterMapping
    @Mapping(target = "cnpj", ignore = true) // Ignorar para que seja preenchido no @AfterMapping
    UserSummaryDTO userToSummaryDTO(User user);

    @AfterMapping
    default void fillPolymorphicFields(@MappingTarget UserSummaryDTO dto, User user) {
        if (user instanceof UserIndividual individual) {
            dto.setCpf(individual.getCpf());
        } else if (user instanceof UserEntity entity) {
            dto.setCnpj(entity.getCnpj());
        }
    }

    @Named("summaryDTOToUser")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User summaryDTOToUser(UserSummaryDTO dto);

    @ObjectFactory
    default User createUser(UserSummaryDTO dto) {
        if (dto.getCpf() != null && !dto.getCpf().isBlank()) {
            return new UserIndividual();
        } else if (dto.getCnpj() != null && !dto.getCnpj().isBlank()) {
            return new UserEntity();
        }
        throw new IllegalArgumentException("UserSummaryDTO must contain either CPF or CNPJ to create an User entity.");
    }

    @Named("summaryDTOToIndividual")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserIndividual summaryDTOToIndividual(UserSummaryDTO dto);

    @Named("summaryDTOToEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    UserEntity summaryDTOToEntity(UserSummaryDTO dto);
}