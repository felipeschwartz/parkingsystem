package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.PaymentDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Payment;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { ParkingSessionMapper.class }
)
public interface PaymentMapper {

    PaymentDTO toDTO(Payment entity);

    @Mapping(target = "id", ignore = true)
    Payment toEntity(PaymentDTO dto);
}