package com.github.felipeschwartz.parkingsystem.controller.docs;

import org.springframework.web.bind.annotation.RequestParam;
import com.github.felipeschwartz.parkingsystem.model.dto.PagedResponse;
import org.springframework.hateoas.EntityModel;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanCreationDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


public interface PlanControllerDocs {
    @Operation(
            summary = "Finds all plans",
            description = "Finds all plans on database.",
            tags = {"Plan"},
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
    PagedResponse<EntityModel<PlanDTO>> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @Operation(
            summary = "Finds a plan",
            description = "Finds a plan by their Id.",
            tags = {"Plan"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PlanDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    EntityModel<PlanDTO> findById(@PathVariable Long id);

    
    
    @Operation(
            summary = "Creates a new plan",
            description = "Creates a new plan with the provided details.",
            tags = {"Plan"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Plan details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PlanDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<EntityModel<PlanDTO>> create(@RequestBody @Valid PlanCreationDTO planCreationDTO);



    @Operation(
            summary = "Updates an existing plan",
            description = "Updates an existing plan identified by their ID.",
            tags = {"Plan"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated plan details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PlanDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    EntityModel<PlanDTO> update(@RequestBody @Valid PlanDTO updatedPlan);



    @Operation(
            summary = "Deletes a plan",
            description = "Deletes a plan identified by their ID.",
            tags = {"Plan"},
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


    @Operation(
            summary = "Activate a plan",
            description = "Activate a plan identified by their ID.",
            tags = {"Plan"},
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
    ResponseEntity<?> activatePlan(@PathVariable Long id);



    @Operation(
            summary = "Deactivate a plan",
            description = "Deactivate a plan identified by their ID.",
            tags = {"Plan"},
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
    ResponseEntity<?> deactivatePlan(@PathVariable Long id);
}
