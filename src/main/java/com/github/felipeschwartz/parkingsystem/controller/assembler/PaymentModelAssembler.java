package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.PaymentController;
import com.github.felipeschwartz.parkingsystem.model.dto.PaymentDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PaymentModelAssembler implements RepresentationModelAssembler<PaymentDTO, EntityModel<PaymentDTO>> {

    @Override
    public EntityModel<PaymentDTO> toModel(PaymentDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(PaymentController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(PaymentController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(PaymentController.class).create(dto)).withRel("create").withType("POST"),
                linkTo(methodOn(PaymentController.class).update(dto)).withRel("update").withType("PUT"));
    }
}
