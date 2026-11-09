package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.PlanController;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlanModelAssembler implements RepresentationModelAssembler<PlanDTO, EntityModel<PlanDTO>> {

    @Override
    public EntityModel<PlanDTO> toModel(PlanDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(PlanController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(PlanController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(PlanController.class).create(null)).withRel("create").withType("POST"),
                linkTo(methodOn(PlanController.class).update(dto)).withRel("update").withType("PUT"),
                linkTo(methodOn(PlanController.class).delete(dto.id())).withRel("delete").withType("DELETE"));
    }
}
