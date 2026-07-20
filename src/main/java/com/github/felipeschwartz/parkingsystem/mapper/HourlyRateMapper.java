package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HourlyRateMapper {

    HourlyRateDTO toDTO(HourlyRate entity);

    @Mapping(target = "id", ignore = true)
    HourlyRate toEntity(HourlyRateDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDto(HourlyRateDTO dto, @MappingTarget HourlyRate entity);
}
