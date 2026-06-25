package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.OwnerEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { AddressMapper.class, VehicleMapper.class }
)
public interface OwnerEntityMapper {
    OwnerEntityDTO toDTO(OwnerEntity entity);

    @Mapping(target = "id", ignore = true)
    OwnerEntity toEntity(OwnerEntityDTO dto);
}
