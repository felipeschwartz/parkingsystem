package com.github.felipeschwartz.parkingsystem.mapper;

import com.github.felipeschwartz.parkingsystem.model.dto.PaymentDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Payment;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { ParkingSessionSummaryMapper.class }
)
public interface PaymentMapper {

    PaymentDTO toDTO(Payment entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parkingSession", ignore = true)
    Payment toEntity(PaymentDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntityFromDTO(PaymentDTO dto, @MappingTarget Payment entity);
}