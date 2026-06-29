package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSpaceDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSpace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { ParkingLotSummaryMapper.class, ReservationMapper.class }
)
public interface ParkingSpaceMapper {

    ParkingSpaceDTO toDTO(ParkingSpace entity);

    @Mapping(target = "id", ignore = true)
    ParkingSpace toEntity(ParkingSpaceDTO dto);
}
