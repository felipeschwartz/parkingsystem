package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.docs.ParkingSpaceControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSpaceDTO;
import com.github.felipeschwartz.parkingsystem.service.ParkingSpaceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/parking_space/v1")
@Tag(name = "Parking Space", description = "Endpoint for managing Parking Spaces")
public class ParkingSpaceController implements ParkingSpaceControllerDocs {

    private final ParkingSpaceService service;

    public ParkingSpaceController(ParkingSpaceService service) {
        this.service = service;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<ParkingSpaceDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ParkingSpaceDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<ParkingSpaceDTO> create(@RequestBody @Valid ParkingSpaceDTO parkingSpace) {
        ParkingSpaceDTO createdParkingSpace = service.createParkingSpace(parkingSpace);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(createdParkingSpace.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdParkingSpace);
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ParkingSpaceDTO update(@RequestBody @Valid ParkingSpaceDTO updatedParkingSpace) {
        return service.updateParkingSpace(updatedParkingSpace);
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteParkingSpace(id);
        return ResponseEntity.noContent().build();
    }

}