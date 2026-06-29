package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.ReservationDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { VehicleSummaryMapper.class, ParkingSpaceSummaryMapper.class }
)
public interface ReservationMapper {

    ReservationDTO toDTO(Reservation entity);

    Reservation toEntity(ReservationDTO dto);
}