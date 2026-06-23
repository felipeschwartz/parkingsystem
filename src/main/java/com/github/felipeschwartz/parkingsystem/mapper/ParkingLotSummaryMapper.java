package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.ParkingLotSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingLot;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ParkingLotSummaryMapper {
    ParkingLotSummaryDTO toSummaryDTO(ParkingLot entity);
}