package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.User;
import com.github.felipeschwartz.parkingsystem.model.entity.UserEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ObjectFactory;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { AddressMapper.class, VehicleMapper.class }
)
public interface UserCreationMapper {

    @ObjectFactory
    default User createUser(CreateUserRequestDTO dto) {
        if (dto.getUserType() == UserType.INDIVIDUAL) {
            return new UserIndividual();
        } else if (dto.getUserType() == UserType.ENTITY) {
            return new UserEntity();
        }
        throw new IllegalArgumentException("Unsupported UserType for creation: " + dto.getUserType());
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "vehicles", source = "vehicles")
    User toEntity(CreateUserRequestDTO dto);

    // Mapeamentos específicos para UserIndividual
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "cpf", source = "cpf")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "vehicles", source = "vehicles")
    UserIndividual toIndividualEntity(CreateUserRequestDTO dto);

    // Mapeamentos específicos para UserEntity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "cnpj", source = "cnpj")
    @Mapping(target = "billingContact", source = "billingContact")
    @Mapping(target = "corporateName", source = "corporateName")
    @Mapping(target = "fantasyName", source = "fantasyName")
    @Mapping(target = "vehicles", source = "vehicles")
    UserEntity toEntityEntity(CreateUserRequestDTO dto);
}