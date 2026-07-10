package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { AddressMapper.class, VehicleMapper.class }
)
public interface UserIndividualMapper {
    UserIndividualDTO toDTO(UserIndividual entity);

    @Mapping(target = "id", ignore = true)
    UserIndividual toEntity(UserIndividualDTO dto);
}