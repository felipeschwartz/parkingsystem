package com.github.felipeschwartz.parkingsystem.controller.docs;

import com.github.felipeschwartz.parkingsystem.model.dto.CloseSessionRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.OpenSessionRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionDTO;
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

public interface ParkingSessionControllerDocs {
    @Operation(
            summary = "Finds all parking sessions",
            description = "Finds all parking sessions on database.",
            tags = {"Parking Session"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = ParkingSessionDTO.class)))}),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<List<ParkingSessionDTO>> findAll();



    @Operation(
            summary = "Finds a parking session",
            description = "Finds a parking session by their Id.",
            tags = {"Parking Session"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ParkingSessionDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<ParkingSessionDTO> findById(@PathVariable Long id);

    
    
    @Operation(
            summary = "Open a new parking session",
            description = "Open a new parking session with the provided details.",
            tags = {"Parking Session"},
            requestBody = @RequestBody(
                    description = "ParkingSession details for opening",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParkingSessionDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ParkingSessionDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<ParkingSessionDTO> openSession(@org.springframework.web.bind.annotation.RequestBody @Valid OpenSessionRequestDTO request);


    @Operation(
            summary = "Closes a parking session",
            description = "Closes a parking session identified by their ID.",
            tags = {"Parking Session"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ParkingSessionDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<ParkingSessionDTO> closeSession(@PathVariable Long id, @RequestBody(required = false) CloseSessionRequestDTO request);



    @Operation(
            summary = "Finds the parking sessions",
            description = "Finds the opened parking sessions on a specific Parking Space by Parking Space Id.",
            tags = {"Parking Session"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ParkingSessionDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<List<ParkingSessionDTO>> findOpenSessionsByParkingSpace(@PathVariable Long parkingSpaceId);

    @Operation(
            summary = "Finds a vehicle parking session",
            description = "Finds a vehicle parking sessions by Vehicle Id.",
            tags = {"Parking Session"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(schema = @Schema(implementation = ParkingSessionDTO.class))
                    ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    ResponseEntity<List<ParkingSessionDTO>> findSessionsByVehicle(@PathVariable Long vehicleId);
}
