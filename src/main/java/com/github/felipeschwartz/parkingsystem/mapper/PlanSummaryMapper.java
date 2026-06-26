package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.PlanSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PlanSummaryMapper {

    @Named("planToSummary")
    PlanSummaryDTO toSummary(Plan entity);

}
