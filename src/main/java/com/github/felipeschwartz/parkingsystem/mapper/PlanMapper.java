package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.*;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.entity.PlanRate;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { PlanRateMapper.class, SubscriptionContractMapper.class },
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface PlanMapper {

    @Mapping(target = "rates", source = "rates", qualifiedByName = "toRateDTOList")
    @Mapping(target = "subscriptionContracts", source = "subscriptionContracts", qualifiedByName = "toSubscriptionContractDTOList")
    PlanDTO toDTO(Plan entity, @Context CycleAvoidingMappingContext context);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptionContracts", ignore = true)
    Plan toEntity(PlanDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rates", ignore = true)
    @Mapping(target = "subscriptionContracts", ignore = true)
    Plan toEntity(PlanCreationDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rates", ignore = true)
    @Mapping(target = "subscriptionContracts", ignore = true)
    void updateEntityFromDTO(PlanCreationDTO dto, @MappingTarget Plan entity, @Context CycleAvoidingMappingContext context);

    @Named("planToPlanDTO")
    default PlanDTO planToPlanDTO(Plan plan, @Context CycleAvoidingMappingContext context) {
        if (plan == null) {
            return null;
        }
        return context.getMappedInstance(plan, PlanDTO.class, () -> toDTO(plan, context));
    }




    @Named("toSubscriptionContractDTOList")
    default Set<SubscriptionContractDTO> mapSubscriptionContracts(Set<SubscriptionContract> contracts, @Context CycleAvoidingMappingContext context) {
        if (contracts == null) {
            return null;
        }
        return contracts.stream()
                .map(contract -> context.getMappedInstance(contract, SubscriptionContractDTO.class, () -> context.getSubscriptionContractMapper().toDTO(contract, context)))
                .collect(Collectors.toSet());
    }

    Plan toEntity(PlanSummaryDTO dto);

    @Named("toRateDTOList")
    default Set<PlanRateDTO> mapRates(Set<PlanRate> rates, @Context CycleAvoidingMappingContext context) {
        if (rates == null) {
            return null;
        }
        return rates.stream()
                .map(rate -> context.getMappedInstance(rate, PlanRateDTO.class, () -> context.getPlanRateMapper().toDTO(rate)))
                .collect(Collectors.toSet());
    }
}