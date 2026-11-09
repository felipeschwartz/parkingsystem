package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.assembler.VehicleModelAssembler;
import org.springframework.hateoas.EntityModel;
import com.github.felipeschwartz.parkingsystem.controller.docs.VehicleControllerDocs;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.service.VehicleService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/vehicle/v1")
@Tag(name = "Vehicle", description = "Endpoint for managing Vehicles")
public class VehicleController implements VehicleControllerDocs {

    private final VehicleService service;
    private final VehicleModelAssembler assembler;

    public VehicleController(VehicleService service, VehicleModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<EntityModel<VehicleDTO>> findAll() {
        return service.findAll().stream().map(assembler::toModel).toList();
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<VehicleDTO> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<VehicleDTO>> create(@RequestBody @Valid VehicleDTO vehicle) {
        VehicleDTO createdVehicle = service.create(vehicle);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(createdVehicle.id())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toModel(createdVehicle));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public EntityModel<VehicleDTO> update(@RequestBody @Valid VehicleDTO updatedVehicle) {
        return assembler.toModel(service.update(updatedVehicle));
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/licence_plate/{licence_plate}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<VehicleDTO> findByLicensePlate(@PathVariable String licence_plate) { return assembler.toModel(service.findByLicensePlate(licence_plate)); }

}