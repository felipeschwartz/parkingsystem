package com.github.felipeschwartz.parkingsystem.controller;

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

    public ParkingSessionController(ParkingSessionService sessionService) {
        this.sessionService = sessionService;
    }

    // CREATE - OPEN SESSION

    @PostMapping("/open")
    @Override
    public ResponseEntity<ParkingSessionDTO> openSession(@RequestBody @Valid OpenSessionRequestDTO request) {
        ParkingSessionDTO newSession = sessionService.openParkingSession(request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/parking_sessions/v1/{id}")
                .buildAndExpand(newSession.getId())
                .toUri();
        log.info("Opened parking session with id={}", newSession.getId());
        return ResponseEntity.created(location).body(newSession);
    }


    // UPDATE - CLOSE SESSION

    @PostMapping("/{id}/close")
    @Override
    public ResponseEntity<ParkingSessionDTO> closeSession(
            @PathVariable("id") Long id,
            @RequestBody(required = false) CloseSessionRequestDTO request
    ) {
        LocalDateTime exitTime = (request != null) ? request.exitTime() : null;
        ParkingSessionDTO closedSession = sessionService.closeSession(id, exitTime);
        log.info("Closed parking session id={} exitTime={}", id, exitTime);
        return ResponseEntity.ok(closedSession);
    }

    // READ SESSIONS

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ParkingSessionDTO> findById(@PathVariable("id") Long id) {
        ParkingSessionDTO session = sessionService.findById(id);
        return ResponseEntity.ok(session);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<ParkingSessionDTO>> findAll() {
        List<ParkingSessionDTO> sessions = sessionService.findAll();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping(value = "/open/space/{parkingSpaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<ParkingSessionDTO>> findOpenSessionsByParkingSpace(@PathVariable("parkingSpaceId") Long parkingSpaceId) {
        List<ParkingSessionDTO> sessions = sessionService.findOpenSessionsByParkingSpace(parkingSpaceId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping(value = "/vehicle/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<ParkingSessionDTO>> findSessionsByVehicle(@PathVariable("vehicleId") Long vehicleId) {
        List<ParkingSessionDTO> sessions = sessionService.findSessionsByVehicle(vehicleId);
        return ResponseEntity.ok(sessions);
    }
}