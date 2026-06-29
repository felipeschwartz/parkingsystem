package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { VehicleSummaryMapper.class, PlanMapper.class, OwnerSummaryMapper.class }
)
public interface SubscriptionContractMapper {

    @Mapping(target = "owner", qualifiedByName = {"ownerToSummaryDTO"})
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = {"vehicleToSummary"})
    SubscriptionContractDTO toDTO(SubscriptionContract entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    SubscriptionContract toEntity(SubscriptionContractDTO dto);
}