package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.UserSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.User;
import com.github.felipeschwartz.parkingsystem.model.entity.UserEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserSummaryMapper {

    @Named("userToSummaryDTO")
    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "cnpj", ignore = true)
    UserSummaryDTO userToSummaryDTO(User user);

    @AfterMapping
    default void fillPolymorphicFields(@MappingTarget UserSummaryDTO dto, User user) {
        if (user instanceof UserIndividual individual) {
            dto.setCpf(individual.getCpf());
        } else if (user instanceof UserEntity entity) {
            dto.setCnpj(entity.getCnpj());
        }
    }


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
    @Mapping(target = "userProfile", ignore = true) // IGNORADO
    @Mapping(target = "roles", ignore = true) // IGNORADO
    @Mapping(target = "firstName", ignore = true) // IGNORADO
    @Mapping(target = "lastName", ignore = true) // IGNORADO
    @Mapping(target = "birthDate", ignore = true) // IGNORADO
    UserIndividual summaryDTOToIndividual(UserSummaryDTO dto);

    @Named("summaryDTOToEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userProfile", ignore = true) // IGNORADO
    @Mapping(target = "roles", ignore = true) // IGNORADO
    @Mapping(target = "billingContact", ignore = true) // IGNORADO
    @Mapping(target = "corporateName", ignore = true) // IGNORADO
    @Mapping(target = "fantasyName", ignore = true) // IGNORADO
    UserEntity summaryDTOToEntity(UserSummaryDTO dto);


}