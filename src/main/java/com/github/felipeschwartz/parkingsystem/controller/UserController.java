package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import com.github.felipeschwartz.parkingsystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints for managing users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Finds all users",
            description = "Finds all users on database.",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<CollectionModel<EntityModel<UserDTO>>> findAll() {
        List<UserDTO> users = service.findAll();
        List<EntityModel<UserDTO>> userModels = users.stream()
                .map(userDTO -> EntityModel.of(userDTO, userDTO.getLinks().stream().collect(Collectors.toList())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(userModels, linkTo(methodOn(UserController.class).findAll()).withSelfRel())
        );
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Finds a user",
            description = "Finds a user by their Id.",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<UserDTO>> findById(@PathVariable Long id) {
        UserDTO userDTO = service.findById(id);
        return ResponseEntity.ok(EntityModel.of(userDTO, userDTO.getLinks().stream().collect(Collectors.toList())));
    }

    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)@Operation(
            summary = "Finds a user",
            description = "Finds a user by their CPF.",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<UserIndividualDTO>> findByCpf(@PathVariable String cpf) {
        UserIndividualDTO userIndividualDTO = service.findByCpf(cpf);
        return ResponseEntity.ok(EntityModel.of(userIndividualDTO, userIndividualDTO.getLinks().stream().collect(Collectors.toList())));
    }

    @GetMapping(value = "/cnpj/{cnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Finds a user",
            description = "Finds a user by their CNPF.",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<UserEntityDTO>> findByCnpj(@PathVariable String cnpj) {
        UserEntityDTO userEntityDTO = service.findByCnpj(cnpj);
        return ResponseEntity.ok(EntityModel.of(userEntityDTO, userEntityDTO.getLinks().stream().collect(Collectors.toList())));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Creates a new user",
            description = "Creates a new user with the provided details.",
            tags = {"User"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "User created successfully",
                            responseCode = "201",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Conflict (e.g., user already exists)", responseCode = "409", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<UserDTO>> create(@RequestBody @Valid CreateUserRequestDTO requestDTO) {
        UserDTO createdUser = service.create(requestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(EntityModel.of(createdUser, createdUser.getLinks().stream().collect(Collectors.toList())));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
            summary = "Updates an existing user",
            description = "Updates an existing user identified by their ID.",
            tags = {"User"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated user details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "User updated successfully",
                            responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<EntityModel<UserDTO>> update(@PathVariable Long id, @RequestBody @Valid CreateUserRequestDTO requestDTO) {
        UserDTO updatedUser = service.update(id, requestDTO);
        return ResponseEntity.ok(EntityModel.of(updatedUser, updatedUser.getLinks().stream().collect(Collectors.toList())));
    }

    @DeleteMapping(value = "/id/{id}")
    @Operation(
            summary = "Deletes a user",
            description = "Deletes a user identified by their ID.",
            tags = {"User"},
            responses = {
                    @ApiResponse(description = "User deleted successfully", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}