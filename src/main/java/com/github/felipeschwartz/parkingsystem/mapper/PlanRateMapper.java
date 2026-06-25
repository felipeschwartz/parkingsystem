package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.PlanRateDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { PlanMapper.class }
)
public interface PlanRateMapper {

    PlanRateDTO toDTO(PlanRate entity);

    @Mapping(target = "id", ignore = true)
    PlanRate toEntity(PlanRateDTO dto);
}
