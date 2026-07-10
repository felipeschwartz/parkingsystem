package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.model.dto.CloseSessionRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.OpenHourlySessionRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.OpenSubscriptionSessionRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionDTO;
import com.github.felipeschwartz.parkingsystem.service.ParkingSessionService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/parking_sessions")
public class ParkingSessionController {

    private final ParkingSessionService sessionService;

    public ParkingSessionController(ParkingSessionService sessionService) {
        this.sessionService = sessionService;
    }

    // CREATE - OPEN SESSIONS

    @PostMapping(
            value = "/open/hourly",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ParkingSessionDTO> openHourlySession(@RequestBody @Valid OpenHourlySessionRequestDTO request) {
        ParkingSessionDTO newSession = sessionService.openHourlySession(
                request.parkingSpaceId(),
                request.licensePlate(),
                request.vehicleType(),
                request.entryTime()
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSession.getId())
                .toUri();
        return ResponseEntity.created(location).body(newSession);
    }

    @PostMapping(
            value = "/open/subscription",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ParkingSessionDTO> openSubscriptionSession(@RequestBody @Valid OpenSubscriptionSessionRequestDTO request) {
        ParkingSessionDTO newSession = sessionService.openSubscriptionSession(
                request.parkingSpaceId(),
                request.vehicleId(),
                request.entryTime()
        );
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newSession.getId())
                .toUri();
        return ResponseEntity.created(location).body(newSession);
    }

    // UPDATE - CLOSE SESSION

    @PutMapping(
            value = "/close/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ParkingSessionDTO> closeSession(
            @PathVariable Long id,
            @RequestBody(required = false) CloseSessionRequestDTO request
    ) {
        LocalDateTime exitTime = (request != null) ? request.exitTime() : null;
        ParkingSessionDTO closedSession = sessionService.closeSession(id, exitTime);
        return ResponseEntity.ok(closedSession);
    }

    // READ SESSIONS

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParkingSessionDTO> findById(@PathVariable Long id) {
        ParkingSessionDTO session = sessionService.findById(id);
        return ResponseEntity.ok(session);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingSessionDTO>> findAll() {
        List<ParkingSessionDTO> sessions = sessionService.findAll();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping(value = "/open/space/{parkingSpaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingSessionDTO>> findOpenSessionsByParkingSpace(@PathVariable Long parkingSpaceId) {
        List<ParkingSessionDTO> sessions = sessionService.findOpenSessionsByParkingSpace(parkingSpaceId);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping(value = "/vehicle/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParkingSessionDTO>> findSessionsByVehicle(@PathVariable Long vehicleId) {
        List<ParkingSessionDTO> sessions = sessionService.findSessionsByVehicle(vehicleId);
        return ResponseEntity.ok(sessions);
    }
}