package com.github.felipeschwartz.parkingsystem.controller.docs;

import org.springframework.web.bind.annotation.RequestParam;
import com.github.felipeschwartz.parkingsystem.model.dto.PagedResponse;
import org.springframework.hateoas.EntityModel;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSpaceDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


public interface ParkingSpaceControllerDocs {
    @Operation(
            summary = "Finds all parking spaces",
            description = "Finds all parking spaces on database.",
            tags = {"Parking Space"},
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
    PagedResponse<EntityModel<ParkingSpaceDTO>> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @Operation(
            summary = "Finds a parking space",
            description = "Finds a parking space by their Id.",
            tags = {"Parking Space"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ParkingSpaceDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    EntityModel<ParkingSpaceDTO> findById(@PathVariable Long id);

    
    
    @Operation(
            summary = "Creates a new parking space",
            description = "Creates a new parking space with the provided details.",
            tags = {"Parking Space"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "ParkingSpace details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingSpaceDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ParkingSpaceDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<EntityModel<ParkingSpaceDTO>> create(@RequestBody @Valid ParkingSpaceDTO parkingSpace);



    @Operation(
            summary = "Updates an existing parking space",
            description = "Updates an existing parking space identified by their ID.",
            tags = {"Parking Space"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated parking space details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingSpaceDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ParkingSpaceDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    EntityModel<ParkingSpaceDTO> update(@RequestBody @Valid ParkingSpaceDTO updatedParkingSpace);



    @Operation(
            summary = "Deletes a parking space",
            description = "Deletes a parking space identified by their ID.",
            tags = {"Parking Space"},
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
    ResponseEntity<?> delete(@PathVariable Long id);
}
