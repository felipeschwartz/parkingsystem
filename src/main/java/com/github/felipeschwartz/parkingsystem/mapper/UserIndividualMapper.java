package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { AddressMapper.class, VehicleMapper.class }
)
public interface UserIndividualMapper {
    @Mapping(source = "userType", target = "userType")
    UserIndividualDTO toDTO(UserIndividual entity);

    @Mapping(target = "id", ignore = true)
    UserIndividual toEntity(UserIndividualDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "cpf", source = "cpf")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "vehicles", ignore = true)
    void updateIndividualFromCreateRequest(CreateUserRequestDTO dto, @MappingTarget UserIndividual entity);
}