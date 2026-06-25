package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.PlanDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { SubscriptionContractMapper.class, PlanRateMapper.class }
)
public interface PlanMapper {

    PlanDTO toDTO(Plan entity);

    @Mapping(target = "id", ignore = true)
    Plan toEntity(PlanDTO dto);
}
