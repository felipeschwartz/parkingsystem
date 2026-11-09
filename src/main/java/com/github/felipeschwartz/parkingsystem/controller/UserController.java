package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.docs.UserControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import com.github.felipeschwartz.parkingsystem.controller.assembler.UserModelAssembler;
import com.github.felipeschwartz.parkingsystem.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/user/v1")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController implements UserControllerDocs {

    private final UserService service;
    private final UserModelAssembler assembler;

    public UserController(UserService service, UserModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> findAll() {
        List<EntityModel<UserDTO>> userModels = service.findAll().stream()
                .<EntityModel<UserDTO>>map(assembler::toModel)
                .toList();

        return ResponseEntity.ok(
                CollectionModel.of(userModels, linkTo(methodOn(UserController.class).findAll()).withSelfRel())
        );
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<EntityModel<UserDTO>> findById(@PathVariable Long id) {
        UserDTO userDTO = service.findById(id);
        return ResponseEntity.ok(assembler.toModel(userDTO));
    }

    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<EntityModel<UserIndividualDTO>> findByCpf(@PathVariable String cpf) {
        UserIndividualDTO userIndividualDTO = service.findByCpf(cpf);
        return ResponseEntity.ok(assembler.toModel(userIndividualDTO));
    }

    @GetMapping(value = "/cnpj/{cnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<EntityModel<UserEntityDTO>> findByCnpj(@PathVariable String cnpj) {
        UserEntityDTO userEntityDTO = service.findByCnpj(cnpj);
        return ResponseEntity.ok(assembler.toModel(userEntityDTO));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<UserDTO>> create(@RequestBody @Valid CreateUserRequestDTO requestDTO) {
        UserDTO createdUser = service.create(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(createdUser.id())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toModel(createdUser));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<UserDTO>> update(@PathVariable Long id, @RequestBody @Valid CreateUserRequestDTO requestDTO) {
        UserDTO updatedUser = service.update(id, requestDTO);
        return ResponseEntity.ok(assembler.toModel(updatedUser));
    }

    @DeleteMapping(value = "/id/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}