package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.OwnerSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Owner;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerIndividual;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OwnerSummaryMapper {

    @Named("ownerToSummaryDTO")
    @Mapping(target = "cpf", ignore = true) // Ignorar para que seja preenchido no @AfterMapping
    @Mapping(target = "cnpj", ignore = true) // Ignorar para que seja preenchido no @AfterMapping
    OwnerSummaryDTO ownerToSummaryDTO(Owner owner);

    @AfterMapping
    default void fillPolymorphicFields(@MappingTarget OwnerSummaryDTO dto, Owner owner) {
        if (owner instanceof OwnerIndividual individual) {
            dto.setCpf(individual.getCpf());
        } else if (owner instanceof OwnerEntity entity) {
            dto.setCnpj(entity.getCnpj());
        }
    }

    @Named("summaryDTOToOwner")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Owner summaryDTOToOwner(OwnerSummaryDTO dto);

    @ObjectFactory
    default Owner createOwner(OwnerSummaryDTO dto) {
        if (dto.getCpf() != null && !dto.getCpf().isBlank()) {
            return new OwnerIndividual();
        } else if (dto.getCnpj() != null && !dto.getCnpj().isBlank()) {
            return new OwnerEntity();
        }
        throw new IllegalArgumentException("OwnerSummaryDTO must contain either CPF or CNPJ to create an Owner entity.");
    }

    @Named("summaryDTOToIndividual")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OwnerIndividual summaryDTOToIndividual(OwnerSummaryDTO dto);

    @Named("summaryDTOToEntity")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OwnerEntity summaryDTOToEntity(OwnerSummaryDTO dto);
}