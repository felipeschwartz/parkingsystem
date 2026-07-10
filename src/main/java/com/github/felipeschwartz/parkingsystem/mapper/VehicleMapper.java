package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { UserSummaryMapper.class, SubscriptionContractMapper.class, ParkingSessionMapper.class }
)
public interface VehicleMapper {

    @Mapping(target = "user", qualifiedByName = {"userToSummaryDTO"})
    @Mapping(target = "subscriptionContracts", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    VehicleDTO toDTO (Vehicle entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "subscriptionContracts", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    Vehicle toEntity(VehicleDTO dto);

    List<VehicleDTO> toDTOList(List<Vehicle> entities);
}