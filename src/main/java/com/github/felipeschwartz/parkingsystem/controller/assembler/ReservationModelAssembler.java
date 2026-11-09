package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.ReservationController;
import com.github.felipeschwartz.parkingsystem.model.dto.ReservationDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReservationModelAssembler implements RepresentationModelAssembler<ReservationDTO, EntityModel<ReservationDTO>> {

    @Override
    public EntityModel<ReservationDTO> toModel(ReservationDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ReservationController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(ReservationController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(ReservationController.class).create(dto)).withRel("create").withType("POST"),
                linkTo(methodOn(ReservationController.class).update(dto)).withRel("update").withType("PUT"),
                linkTo(methodOn(ReservationController.class).delete(dto.id())).withRel("delete").withType("DELETE"));
    }
}
