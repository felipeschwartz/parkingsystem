package com.github.felipeschwartz.parkingsystem.controller.assembler;

import com.github.felipeschwartz.parkingsystem.controller.UserController;
import com.github.felipeschwartz.parkingsystem.model.dto.UserDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Generic over the concrete user type so endpoints that return {@link UserIndividualDTO} or
 * {@link UserEntityDTO} keep their precise type in the response model.
 */
@Component
public class UserModelAssembler {

    public <T extends UserDTO> EntityModel<T> toModel(T dto) {
        List<Link> links = new ArrayList<>();
        links.add(linkTo(methodOn(UserController.class).findById(dto.id())).withSelfRel().withType("GET"));
        links.add(linkTo(methodOn(UserController.class).findAll()).withRel("findAllUsers").withType("GET"));
        links.add(linkTo(methodOn(UserController.class).create(null)).withRel("createUser").withType("POST"));
        links.add(linkTo(methodOn(UserController.class).update(dto.id(), null)).withRel("updateUser").withType("PUT"));
        links.add(linkTo(methodOn(UserController.class).delete(dto.id())).withRel("deleteUser").withType("DELETE"));
        if (dto instanceof UserIndividualDTO individual && individual.cpf() != null) {
            links.add(linkTo(methodOn(UserController.class).findByCpf(individual.cpf())).withRel("findByCpf").withType("GET"));
        } else if (dto instanceof UserEntityDTO entity && entity.cnpj() != null) {
            links.add(linkTo(methodOn(UserController.class).findByCnpj(entity.cnpj())).withRel("findByCnpj").withType("GET"));
        }
        return EntityModel.of(dto, links);
    }
}
