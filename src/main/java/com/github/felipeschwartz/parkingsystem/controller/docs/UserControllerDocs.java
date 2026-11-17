package com.github.felipeschwartz.parkingsystem.controller.docs;

import org.springframework.web.bind.annotation.RequestParam;
import com.github.felipeschwartz.parkingsystem.model.dto.PagedResponse;
import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface UserControllerDocs {
    @Operation(
            summary = "Finds all users",
            description = "Finds all users on database.",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = PagedResponse.class))}),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<PagedResponse<EntityModel<UserDTO>>> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @Operation(
            summary = "Finds a user",
            description = "Finds a user by their Id.",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<EntityModel<UserDTO>> findById(@PathVariable Long id);

    @Operation(
            summary = "Finds a user",
            description = "Finds a user by their CPF.",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<EntityModel<UserIndividualDTO>> findByCpf(@PathVariable String cpf);

    @Operation(
            summary = "Finds a user",
            description = "Finds a user by their CNPF.",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<EntityModel<UserEntityDTO>> findByCnpj(@PathVariable String cnpj);

    @Operation(
            summary = "Creates a new user",
            description = "Creates a new user with the provided details.",
            tags = {"User"},
            requestBody = @RequestBody(
                    description = "User details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<EntityModel<UserDTO>> create(@RequestBody @Valid CreateUserRequestDTO requestDTO);

    @Operation(
            summary = "Updates an existing user",
            description = "Updates an existing user identified by their ID.",
            tags = {"User"},
            requestBody = @RequestBody(
                    description = "Updated user details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CreateUserRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<EntityModel<UserDTO>> update(@PathVariable Long id, @RequestBody @Valid CreateUserRequestDTO requestDTO);

    @Operation(
            summary = "Deletes a user",
            description = "Deletes a user identified by their ID.",
            tags = {"User"},
            responses = {
                    @ApiResponse(
                            description = "No Content",
                            responseCode = "204",
                            content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<Void> delete(@PathVariable Long id);
}
