package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.OwnerIndividualDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerIndividual;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { AddressMapper.class, VehicleMapper.class }
)
public interface OwnerIndividualMapper {
    OwnerIndividualDTO toDTO(OwnerIndividual owner);

    @Mapping(target = "id", ignore = true)
    OwnerIndividual toEntity(OwnerIndividualDTO dto);
}