package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.docs.ParkingLotControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingLotDTO;
import com.github.felipeschwartz.parkingsystem.service.ParkingLotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/parking_lot/v1")
@Tag(name = "Parking Lot", description = "Endpoint for managing Parking Lots")
public class ParkingLotController implements ParkingLotControllerDocs {

    private final ParkingLotService service;

    public ParkingLotController(ParkingLotService service) {
        this.service = service;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<ParkingLotDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ParkingLotDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<ParkingLotDTO> create(@RequestBody @Valid ParkingLotDTO parkingLot) {
        ParkingLotDTO createdParkingLot = service.create(parkingLot);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/id/{id}")
                .buildAndExpand(createdParkingLot.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdParkingLot);
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ParkingLotDTO update(@RequestBody @Valid ParkingLotDTO updatedParkingLot) {
        return service.update(updatedParkingLot);
    }


    @DeleteMapping(value = "/id/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}