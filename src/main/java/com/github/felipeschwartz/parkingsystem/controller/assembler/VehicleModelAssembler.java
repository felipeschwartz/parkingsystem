package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.VehicleController;
import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VehicleModelAssembler implements RepresentationModelAssembler<VehicleDTO, EntityModel<VehicleDTO>> {

    @Override
    public EntityModel<VehicleDTO> toModel(VehicleDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(VehicleController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(VehicleController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(VehicleController.class).create(dto)).withRel("create").withType("POST"),
                linkTo(methodOn(VehicleController.class).update(dto)).withRel("update").withType("PUT"),
                linkTo(methodOn(VehicleController.class).delete(dto.id())).withRel("delete").withType("DELETE"));
    }
}
