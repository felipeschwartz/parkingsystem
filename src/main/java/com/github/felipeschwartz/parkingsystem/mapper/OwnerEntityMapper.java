package com.github.felipeschwartz.parkingsystem.mapper;

import org.mapstruct.Mapper;
import com.github.felipeschwartz.parkingsystem.model.dto.OwnerEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerEntity;

@Mapper(componentModel = "spring")
public interface OwnerEntityMapper {
    OwnerEntityDTO toDTO(OwnerEntity owner);
    OwnerEntity toEntity(OwnerEntityDTO dto);
}
