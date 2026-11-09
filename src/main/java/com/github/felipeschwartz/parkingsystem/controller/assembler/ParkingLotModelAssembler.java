package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.ParkingLotController;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingLotDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ParkingLotModelAssembler implements RepresentationModelAssembler<ParkingLotDTO, EntityModel<ParkingLotDTO>> {

    @Override
    public EntityModel<ParkingLotDTO> toModel(ParkingLotDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ParkingLotController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(ParkingLotController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(ParkingLotController.class).create(dto)).withRel("create").withType("POST"),
                linkTo(methodOn(ParkingLotController.class).update(dto)).withRel("update").withType("PUT"),
                linkTo(methodOn(ParkingLotController.class).delete(dto.id())).withRel("delete").withType("DELETE"));
    }
}
