package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.SubscriptionContractController;
import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SubscriptionContractModelAssembler implements RepresentationModelAssembler<SubscriptionContractDTO, EntityModel<SubscriptionContractDTO>> {

    @Override
    public EntityModel<SubscriptionContractDTO> toModel(SubscriptionContractDTO dto) {
        return EntityModel.of(dto,
                linkTo(methodOn(SubscriptionContractController.class).findById(dto.id())).withSelfRel().withType("GET"),
                linkTo(methodOn(SubscriptionContractController.class).findAll()).withRel("findAll").withType("GET"),
                linkTo(methodOn(SubscriptionContractController.class).create(dto)).withRel("create").withType("POST"),
                linkTo(methodOn(SubscriptionContractController.class).update(dto)).withRel("update").withType("PUT"),
                linkTo(methodOn(SubscriptionContractController.class).renew(dto.id(), dto)).withRel("renew").withType("PUT"),
                linkTo(methodOn(SubscriptionContractController.class).delete(dto.id())).withRel("delete").withType("DELETE"));
    }
}
