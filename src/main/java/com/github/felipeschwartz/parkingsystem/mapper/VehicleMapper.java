package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { OwnerSummaryMapper.class, SubscriptionContractMapper.class, ParkingSessionMapper.class }
)
public interface VehicleMapper {

    @Mapping(target = "owner", qualifiedByName = {"ownerToSummaryDTO"})
    @Mapping(target = "subscriptionContracts", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    VehicleDTO toDTO (Vehicle entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "subscriptionContracts", ignore = true)
    @Mapping(target = "sessions", ignore = true)
    Vehicle toEntity(VehicleDTO dto);

    List<VehicleDTO> toDTOList(List<Vehicle> entities);
}