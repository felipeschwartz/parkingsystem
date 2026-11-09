package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.HourlyRateController;
import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class HourlyRateModelAssembler implements RepresentationModelAssembler<HourlyRateDTO, EntityModel<HourlyRateDTO>> {

    @Override
    public EntityModel<HourlyRateDTO> toModel(HourlyRateDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(HourlyRateController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(HourlyRateController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(HourlyRateController.class).create(dto)).withRel("create").withType("POST"),
                linkTo(methodOn(HourlyRateController.class).update(dto)).withRel("update").withType("PUT"),
                linkTo(methodOn(HourlyRateController.class).delete(dto.id())).withRel("delete").withType("DELETE"));
    }
}
