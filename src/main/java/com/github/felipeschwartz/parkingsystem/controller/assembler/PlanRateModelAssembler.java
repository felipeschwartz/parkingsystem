package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.PlanRateController;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanRateDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PlanRateModelAssembler implements RepresentationModelAssembler<PlanRateDTO, EntityModel<PlanRateDTO>> {

    @Override
    public EntityModel<PlanRateDTO> toModel(PlanRateDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(PlanRateController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(PlanRateController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(PlanRateController.class).create(dto)).withRel("create").withType("POST"),
                linkTo(methodOn(PlanRateController.class).update(dto)).withRel("update").withType("PUT"),
                linkTo(methodOn(PlanRateController.class).delete(dto.id())).withRel("delete").withType("DELETE"));
    }
}
