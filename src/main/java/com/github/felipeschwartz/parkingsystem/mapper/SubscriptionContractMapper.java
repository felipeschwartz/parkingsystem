package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.UserSummaryDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.*;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { VehicleSummaryMapper.class, UserSummaryMapper.class, PlanMapper.class }
)
public interface SubscriptionContractMapper {

    @Mapping(target = "user", source = "user")
    @Mapping(target = "vehicle", source = "vehicle", qualifiedByName = {"vehicleToSummary"})
    @Mapping(target = "plan", source = "plan")
    SubscriptionContractDTO toDTO(SubscriptionContract entity, @Context CycleAvoidingMappingContext context);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "plan", source = "plan")
    SubscriptionContract toEntity(SubscriptionContractDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "plan", ignore = true)
    void updateSubscriptionContractFromDto(SubscriptionContractDTO dto, @MappingTarget SubscriptionContract entity);

    default UserSummaryDTO mapUserToSummaryDTO(User user) {
        if (user == null) {
            return null;
        }
        UserSummaryDTO dto = new UserSummaryDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        if (user instanceof UserIndividual individual) {
            dto.setCpf(individual.getCpf());
        } else if (user instanceof UserEntity entity) {
            dto.setCnpj(entity.getCnpj());
        }
        return dto;
    }
}