package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.ParkingSessionController;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ParkingSessionModelAssembler implements RepresentationModelAssembler<ParkingSessionDTO, EntityModel<ParkingSessionDTO>> {

    @Override
    public EntityModel<ParkingSessionDTO> toModel(ParkingSessionDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(ParkingSessionController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(ParkingSessionController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(ParkingSessionController.class).openSession(null)).withRel("create").withType("POST"),
                linkTo(methodOn(ParkingSessionController.class).closeSession(dto.id(), null)).withRel("close").withType("POST"));
    }
}
