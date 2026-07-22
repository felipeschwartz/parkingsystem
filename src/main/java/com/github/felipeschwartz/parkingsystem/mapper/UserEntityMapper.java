package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { AddressMapper.class, VehicleMapper.class }
)
public interface UserEntityMapper {
    @Mapping(source = "userType", target = "userType")
    UserEntityDTO toDTO(UserEntity entity);

    @Mapping(target = "id", ignore = true)
    UserEntity toEntity(UserEntityDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "cnpj", source = "cnpj")
    @Mapping(target = "billingContact", source = "billingContact")
    @Mapping(target = "corporateName", source = "corporateName")
    @Mapping(target = "fantasyName", source = "fantasyName")
    @Mapping(target = "vehicles", ignore = true)
    void updateEntityFromCreateRequest(CreateUserRequestDTO dto, @MappingTarget UserEntity entity);
}
