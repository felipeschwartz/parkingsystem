package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.VehicleSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VehicleSummaryMapper {

    @Named("vehicleToSummary")
    VehicleSummaryDTO toSummary(Vehicle entity);

}