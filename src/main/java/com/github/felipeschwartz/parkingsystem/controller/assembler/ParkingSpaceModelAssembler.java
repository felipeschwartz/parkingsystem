package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.ParkingSpaceController;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSpaceDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ParkingSpaceModelAssembler implements RepresentationModelAssembler<ParkingSpaceDTO, EntityModel<ParkingSpaceDTO>> {

    @Override
    public EntityModel<ParkingSpaceDTO> toModel(ParkingSpaceDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ParkingSpaceController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(ParkingSpaceController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(ParkingSpaceController.class).create(dto)).withRel("create").withType("POST"),
                linkTo(methodOn(ParkingSpaceController.class).update(dto)).withRel("update").withType("PUT"),
                linkTo(methodOn(ParkingSpaceController.class).delete(dto.id())).withRel("delete").withType("DELETE"));
    }
}
