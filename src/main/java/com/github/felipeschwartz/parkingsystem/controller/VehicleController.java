package com.github.felipeschwartz.parkingsystem.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Vehicle;
import com.github.felipeschwartz.parkingsystem.service.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VehicleDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<VehicleDTO> create(@RequestBody @Valid Vehicle vehicle) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(vehicle));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public VehicleDTO update(@PathVariable Long id, @RequestBody @Valid Vehicle updatedVehicle) {
        return service.update(id, updatedVehicle);
    }


    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/licence_plate/{licence_plate}", produces = MediaType.APPLICATION_JSON_VALUE)
    public VehicleDTO findByLicensePlate(@PathVariable String licence_plate) { return service.findByLicensePlate(licence_plate); }

}