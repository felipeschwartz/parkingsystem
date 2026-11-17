package com.github.felipeschwartz.parkingsystem.controller.docs;

import org.springframework.web.bind.annotation.RequestParam;
import com.github.felipeschwartz.parkingsystem.model.dto.PagedResponse;
import org.springframework.hateoas.EntityModel;
import com.github.felipeschwartz.parkingsystem.model.dto.PaymentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


public interface PaymentControllerDocs {
    @Operation(
            summary = "Finds all payments",
            description = "Finds all payments on database.",
            tags = {"Payment"},
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
    PagedResponse<EntityModel<PaymentDTO>> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @Operation(
            summary = "Finds a payment",
            description = "Finds a payment by their Id.",
            tags = {"Payment"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PaymentDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    EntityModel<PaymentDTO> findById(@PathVariable Long id);

    
    
    @Operation(
            summary = "Creates a new payment",
            description = "Creates a new payment with the provided details.",
            tags = {"Payment"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment details for creation",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PaymentDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<EntityModel<PaymentDTO>> create(@RequestBody @Valid PaymentDTO payment);



    @Operation(
            summary = "Updates an existing payment",
            description = "Updates an existing payment identified by their ID.",
            tags = {"Payment"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated payment details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = PaymentDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    EntityModel<PaymentDTO> update(@RequestBody @Valid PaymentDTO updatedPayment);
}
