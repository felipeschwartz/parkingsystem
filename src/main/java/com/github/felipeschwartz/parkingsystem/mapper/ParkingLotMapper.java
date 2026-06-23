package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.ParkingLotDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.ParkingLot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { AddressMapper.class, ParkingSpaceMapper.class }
)
public interface ParkingLotMapper {

    ParkingLotDTO toDTO(ParkingLot entity);

    @Mapping(target = "id", ignore = true)
    ParkingLot toEntity(ParkingLotDTO dto);
}
