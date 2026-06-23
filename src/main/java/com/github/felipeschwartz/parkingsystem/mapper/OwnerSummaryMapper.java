package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.OwnerSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Owner;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerIndividual;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OwnerSummaryMapper {

    @Mapping(target = "cpf", ignore = true)
    @Mapping(target = "cnpj", ignore = true)
    OwnerSummaryDTO toSummaryDTO(Owner owner);

    default OwnerSummaryDTO toSummaryDTOPolymorphic(Owner owner) {
        OwnerSummaryDTO dto = toSummaryDTO(owner);
        if (owner instanceof OwnerIndividual individual) {
            dto.setCpf(individual.getCpf());
        } else if (owner instanceof OwnerEntity entity) {
            dto.setCnpj(entity.getCnpj());
        }
        return dto;
    }
}