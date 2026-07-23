package com.github.felipeschwartz.parkingsystem.controller.docs;


import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;

public interface SubscriptionContractControllerDocs {
    @Operation(
            summary = "Finds all subscription contracts",
            description = "Finds all subscription contracts on database.",
            tags = {"Subscription Contract"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = SubscriptionContractDTO.class)))}),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    List<SubscriptionContractDTO> findAll();

    @Operation(
            summary = "Finds a subscription contract",
            description = "Finds a subscription contract by their Id.",
            tags = {"Subscription Contract"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = SubscriptionContractDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    SubscriptionContractDTO findById(@PathVariable Long id);

    
    
    @Operation(
            summary = "Creates a new subscription contract",
            description = "Creates a new subscription contract with the provided details.",
            tags = {"Subscription Contract"},
            requestBody = @RequestBody(
                    description = "SubscriptionContract details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionContractDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = SubscriptionContractDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<SubscriptionContractDTO> create(@RequestBody @Valid SubscriptionContractDTO subscriptionContractDTO);



    @Operation(
            summary = "Updates an existing subscription contract",
            description = "Updates an existing subscription contract identified by their ID.",
            tags = {"Subscription Contract"},
            requestBody = @RequestBody(
                    description = "Updated subscription contract details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionContractDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = SubscriptionContractDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<SubscriptionContractDTO> update(@RequestBody @Valid SubscriptionContractDTO subscriptionContractDTO);



    @Operation(
            summary = "Renew an existing subscription contract",
            description = "Renew an existing subscription contract identified by their ID.",
            tags = {"Subscription Contract"},
            requestBody = @RequestBody(
                    description = "Updated subscription contract details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionContractDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = SubscriptionContractDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<SubscriptionContractDTO> renew(@PathVariable Long id, @RequestBody SubscriptionContractDTO subscriptionContractDTO);



    @Operation(
            summary = "Deletes a subscription contract",
            description = "Deletes a subscription contract identified by their ID.",
            tags = {"Subscription Contract"},
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
