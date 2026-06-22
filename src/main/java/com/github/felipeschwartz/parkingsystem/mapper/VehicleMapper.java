package com.github.felipeschwartz.parkingsystem.mapper;

import org.mapstruct.Mapper;
import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    VehicleDTO toDTO(Vehicle vehicle);
    Vehicle toEntity(VehicleDTO dto);
}
