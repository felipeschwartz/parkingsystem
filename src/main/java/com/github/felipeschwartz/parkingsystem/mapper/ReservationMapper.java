package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.ReservationDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Reservation;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { VehicleSummaryMapper.class, ParkingSpaceSummaryMapper.class }
)
public interface ReservationMapper {

    ReservationDTO toDTO(Reservation entity);

    Reservation toEntity(ReservationDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReservationFromDto(ReservationDTO dto, @MappingTarget Reservation entity);
}