package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.PlanRateDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { PlanSummaryMapper.class }
)
public interface PlanRateMapper {

    PlanRateDTO toDTO(PlanRate entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "plan", source = "plan")
    PlanRate toEntity(PlanRateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "plan", ignore = true)
    void updatePlanRateFromDto(PlanRateDTO dto, @MappingTarget PlanRate entity);
}
