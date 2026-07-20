package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSession;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ParkingSessionSummaryMapper {
    ParkingSessionSummaryDTO toDTO(ParkingSession entity);
}