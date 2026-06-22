package com.github.felipeschwartz.parkingsystem.mapper;

import org.mapstruct.Mapper;
import com.github.felipeschwartz.parkingsystem.model.dto.OwnerIndividualDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerIndividual;

@Mapper(componentModel = "spring")
public interface OwnerIndividualMapper {
    OwnerIndividualDTO toDTO(OwnerIndividual owner);
    OwnerIndividual toEntity(OwnerIndividualDTO dto);
}
