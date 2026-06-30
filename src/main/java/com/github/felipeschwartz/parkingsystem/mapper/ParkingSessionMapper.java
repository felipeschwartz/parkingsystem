package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSession;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { VehicleSummaryMapper.class, ParkingSpaceMapper.class, PaymentMapper.class }
)
public interface ParkingSessionMapper {

    ParkingSessionDTO toDTO(ParkingSession entity);

    @Mapping(target = "id", ignore = true)
    ParkingSession toEntity(ParkingSessionDTO dto);
}