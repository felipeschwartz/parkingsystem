package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.felipeschwartz.parkingsystem.model.dto.UserDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.UserEntity;
import com.github.felipeschwartz.parkingsystem.service.UserService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> findAll() {
        List<UserDTO> users = service.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        return service.findUserById(id)
                .map(userDTO -> new ResponseEntity<>(userDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserIndividualDTO> findByCpf(@PathVariable String cpf) {
        return service.findUserByCpf(cpf)
                .map(userIndividualDTO -> new ResponseEntity<>(userIndividualDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/cnpj/{cnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserEntityDTO> findByCnpj(@PathVariable String cnpj) {
        return service.findUserByCnpj(cnpj)
                .map(userEntityDTO -> new ResponseEntity<>(userEntityDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(
            value = "/entity",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserEntityDTO> createEntity(@RequestBody @Valid UserEntity entity) {
        UserEntityDTO createdEntity = service.createEntity(entity);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEntity.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdEntity);
    }

    @PostMapping(
            value = "/individual",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserIndividualDTO> createIndividual(@RequestBody @Valid UserIndividual individual) {
        UserIndividualDTO createdIndividual = service.createIndividual(individual);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}") // CORRIGIDO: Adicionado o fechamento da chave
                .buildAndExpand(createdIndividual.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdIndividual);
    }

    @PutMapping(
            value = "/individuals/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserIndividualDTO> updateIndividuals(@PathVariable Long id, @RequestBody @Valid UserIndividualDTO updatedIndividual) {
        Optional<UserIndividualDTO> updatedUser = service.updateIndividual(id, updatedIndividual);
        return updatedUser.map(userIndividualDTO -> new ResponseEntity<>(userIndividualDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(
            value = "/entities/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserEntityDTO> updateEntities(@PathVariable Long id, @RequestBody @Valid UserEntityDTO updatedEntity) {
        Optional<UserEntityDTO> updatedUser = service.updateEntity(id, updatedEntity);
        return updatedUser.map(userEntityDTO -> new ResponseEntity<>(userEntityDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}