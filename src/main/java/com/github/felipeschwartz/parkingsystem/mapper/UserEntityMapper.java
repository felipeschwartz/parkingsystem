package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { AddressMapper.class, VehicleMapper.class }
)
public interface UserEntityMapper {
    @Mapping(source = "userType", target = "userType")
    UserEntityDTO toDTO(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserEntityDTO dto);
}
