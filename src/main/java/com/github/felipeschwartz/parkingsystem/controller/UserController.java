package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import com.github.felipeschwartz.parkingsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/user") // Mantendo "/user" como no seu arquivo atual
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> findAll() {
        List<UserDTO> users = service.findAll(); // O service.findAll() agora retorna List<UserDTO>
        List<EntityModel<UserDTO>> userModels = users.stream()
                .map(userDTO -> EntityModel.of(userDTO, userDTO.getLinks().stream().collect(Collectors.toList())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(userModels, linkTo(methodOn(UserController.class).findAll()).withSelfRel())
        );
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<UserDTO>> findById(@PathVariable Long id) {
        UserDTO userDTO = service.findById(id); // O service.findById() agora retorna UserDTO diretamente
        return ResponseEntity.ok(EntityModel.of(userDTO, userDTO.getLinks().stream().collect(Collectors.toList())));
    }

    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<UserIndividualDTO>> findByCpf(@PathVariable String cpf) {
        UserIndividualDTO userIndividualDTO = service.findByCpf(cpf); // O service.findByCpf() agora retorna UserIndividualDTO diretamente
        return ResponseEntity.ok(EntityModel.of(userIndividualDTO, userIndividualDTO.getLinks().stream().collect(Collectors.toList())));
    }

    @GetMapping(value = "/cnpj/{cnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<UserEntityDTO>> findByCnpj(@PathVariable String cnpj) {
        UserEntityDTO userEntityDTO = service.findByCnpj(cnpj); // O service.findByCnpj() agora retorna UserEntityDTO diretamente
        return ResponseEntity.ok(EntityModel.of(userEntityDTO, userEntityDTO.getLinks().stream().collect(Collectors.toList())));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EntityModel<UserDTO>> create(@RequestBody @Valid CreateUserRequestDTO requestDTO) {
        UserDTO createdUser = service.create(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/id/{id}") // Ajustado para /id/{id} conforme seu padrão
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(EntityModel.of(createdUser, createdUser.getLinks().stream().collect(Collectors.toList())));
    }

    @PutMapping(
            value = "/{id}", // Endpoint unificado para update
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EntityModel<UserDTO>> update(@PathVariable Long id, @RequestBody @Valid CreateUserRequestDTO requestDTO) {
        UserDTO updatedUser = service.update(id, requestDTO);
        return ResponseEntity.ok(EntityModel.of(updatedUser, updatedUser.getLinks().stream().collect(Collectors.toList())));
    }

    @DeleteMapping(value = "/id/{id}") // Mantendo "/id/{id}" como no seu arquivo atual
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}