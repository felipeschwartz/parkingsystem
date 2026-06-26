package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSpaceSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ParkingSpaceSummaryMapper {

    @Named("parkingSpaceToSummary")
    ParkingSpaceSummaryDTO toSummary(ParkingSpace entity);

}