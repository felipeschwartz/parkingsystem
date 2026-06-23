package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.ReservationDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingSession;
import com.github.felipeschwartz.parkingsystem.model.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { VehicleMapper.class, ParkingSpaceMapper.class, PaymentMapper.class }
)
public interface ParkingSessionMapper {

    ParkingSessionDTO toDTO(Reservation entity);

    @Mapping(target = "id", ignore = true)
    ParkingSession toEntity(ReservationDTO dto);
}