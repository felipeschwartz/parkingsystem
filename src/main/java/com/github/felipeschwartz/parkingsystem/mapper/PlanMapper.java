package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.PlanCreationDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { PlanRateMapper.class },
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface PlanMapper {

    PlanDTO toDTO(Plan entity, @Context CycleAvoidingMappingContext context);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptionContracts", ignore = true)
    Plan toEntity(PlanDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptionContracts", ignore = true)
    Plan toEntity(PlanCreationDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptionContracts", ignore = true)
    void updateEntityFromDTO(PlanCreationDTO dto, @MappingTarget Plan entity, @Context CycleAvoidingMappingContext context);

    @Named("planToPlanDTO")
    default PlanDTO planToPlanDTO(Plan plan, @Context CycleAvoidingMappingContext context) {
        if (plan == null) {
            return null;
        }
        return context.getMappedInstance(plan, PlanDTO.class, () -> toDTO(plan, context));
    }

    default Set<SubscriptionContractDTO> mapSubscriptionContracts(Set<SubscriptionContract> contracts, @Context CycleAvoidingMappingContext context) {
        if (contracts == null) {
            return null;
        }
        return contracts.stream()
                .map(contract -> context.getMappedInstance(contract, SubscriptionContractDTO.class, () -> context.getSubscriptionContractMapper().toDTO(contract, context)))
                .collect(Collectors.toSet()); // Use toSet() para Set
    }

    Plan toEntity(PlanSummaryDTO dto);
}