package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.assembler.ParkingSpaceModelAssembler;
import org.springframework.hateoas.EntityModel;
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
    private final ParkingSpaceModelAssembler assembler;

    public ParkingSpaceController(ParkingSpaceService service, ParkingSpaceModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<EntityModel<ParkingSpaceDTO>> findAll() {
        return service.findAll().stream().map(assembler::toModel).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<ParkingSpaceDTO> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<ParkingSpaceDTO>> create(@RequestBody @Valid ParkingSpaceDTO parkingSpace) {
        ParkingSpaceDTO createdParkingSpace = service.createParkingSpace(parkingSpace);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdParkingSpace.id())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toModel(createdParkingSpace));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public EntityModel<ParkingSpaceDTO> update(@RequestBody @Valid ParkingSpaceDTO updatedParkingSpace) {
        return assembler.toModel(service.updateParkingSpace(updatedParkingSpace));
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deleteParkingSpace(id);
        return ResponseEntity.noContent().build();
    }

}