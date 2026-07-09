package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.OwnerSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Owner;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerIndividual;
import com.github.felipeschwartz.parkingsystem.model.entity.SubscriptionContract;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { VehicleSummaryMapper.class, OwnerSummaryMapper.class, PlanMapper.class }
)
public interface SubscriptionContractMapper {

    @Mapping(target = "owner", source = "owner")
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = {"vehicleToSummary"})
    @Mapping(target = "plan", source = "plan")
    SubscriptionContractDTO toDTO(SubscriptionContract entity, @Context CycleAvoidingMappingContext context);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "plan", source = "plan")
    SubscriptionContract toEntity(SubscriptionContractDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "plan", ignore = true)
    void updateSubscriptionContractFromDto(SubscriptionContractDTO dto, @MappingTarget SubscriptionContract entity);

    default OwnerSummaryDTO mapOwnerToSummaryDTO(Owner owner) {
        if (owner == null) {
            return null;
        }
        OwnerSummaryDTO dto = new OwnerSummaryDTO();
        dto.setId(owner.getId());
        dto.setEmail(owner.getEmail());
        dto.setPhone(owner.getPhone());

        if (owner instanceof OwnerIndividual individual) {
            dto.setCpf(individual.getCpf());
        } else if (owner instanceof OwnerEntity entity) {
            dto.setCnpj(entity.getCnpj());
        }
        return dto;
    }
}