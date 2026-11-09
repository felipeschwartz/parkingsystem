package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.assembler.ParkingSessionModelAssembler;
import org.springframework.hateoas.EntityModel;
import com.github.felipeschwartz.parkingsystem.controller.docs.ParkingSessionControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.*;
import com.github.felipeschwartz.parkingsystem.service.ParkingSessionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/parking_sessions/v1")
@Tag(name = "Parking Session", description = "Endpoint for managing Parking Sessions")
@Validated
public class ParkingSessionController implements ParkingSessionControllerDocs {

    private static final Logger log = LoggerFactory.getLogger(ParkingSessionController.class);
    private final ParkingSessionService sessionService;
    private final ParkingSessionModelAssembler assembler;

    public ParkingSessionController(ParkingSessionService sessionService, ParkingSessionModelAssembler assembler) {
        this.sessionService = sessionService;
        this.assembler = assembler;
    }


    @PostMapping("/open")
    @Override
    public ResponseEntity<EntityModel<ParkingSessionDTO>> openSession(@RequestBody @Valid OpenSessionRequestDTO request) {
        ParkingSessionDTO newSession = sessionService.openParkingSession(request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/parking_sessions/v1/{id}")
                .buildAndExpand(newSession.id())
                .toUri();
        log.info("Opened parking session with id={}", newSession.id());
        return ResponseEntity.created(location).body(assembler.toModel(newSession));
    }



    @PostMapping("/{id}/close")
    @Override
    public ResponseEntity<EntityModel<ParkingSessionDTO>> closeSession(
            @PathVariable("id") Long id,
            @RequestBody(required = false) CloseSessionRequestDTO request
    ) {
        LocalDateTime exitTime = (request != null) ? request.exitTime() : null;
        ParkingSessionDTO closedSession = sessionService.closeSession(id, exitTime);
        log.info("Closed parking session id={} exitTime={}", id, exitTime);
        return ResponseEntity.ok(assembler.toModel(closedSession));
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<EntityModel<ParkingSessionDTO>> findById(@PathVariable("id") Long id) {
        ParkingSessionDTO session = sessionService.findById(id);
        return ResponseEntity.ok(assembler.toModel(session));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<EntityModel<ParkingSessionDTO>>> findAll() {
        List<ParkingSessionDTO> sessions = sessionService.findAll();
        return ResponseEntity.ok(sessions.stream().map(assembler::toModel).toList());
    }

    @GetMapping(value = "/open/space/{parkingSpaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<EntityModel<ParkingSessionDTO>>> findOpenSessionsByParkingSpace(@PathVariable("parkingSpaceId") Long parkingSpaceId) {
        List<ParkingSessionDTO> sessions = sessionService.findOpenSessionsByParkingSpace(parkingSpaceId);
        return ResponseEntity.ok(sessions.stream().map(assembler::toModel).toList());
    }

    @GetMapping(value = "/vehicle/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<EntityModel<ParkingSessionDTO>>> findSessionsByVehicle(@PathVariable("vehicleId") Long vehicleId) {
        List<ParkingSessionDTO> sessions = sessionService.findSessionsByVehicle(vehicleId);
        return ResponseEntity.ok(sessions.stream().map(assembler::toModel).toList());
    }
}