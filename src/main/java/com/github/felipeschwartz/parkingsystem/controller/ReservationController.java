package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.assembler.ReservationModelAssembler;
import org.springframework.hateoas.EntityModel;
import com.github.felipeschwartz.parkingsystem.controller.docs.ReservationControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.ReservationDTO;
import com.github.felipeschwartz.parkingsystem.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/reservation/v1")
@Tag(name = "Reservation", description = "Endpoint for managing Reservations")
public class ReservationController implements ReservationControllerDocs {

    private final ReservationService service;
    private final ReservationModelAssembler assembler;

    public ReservationController(ReservationService service, ReservationModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<EntityModel<ReservationDTO>> findAll() {
        return service.findAll().stream().map(assembler::toModel).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<ReservationDTO> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<ReservationDTO>> create(@RequestBody @Valid ReservationDTO reservationDTO) {
        ReservationDTO createdReservation = service.create(reservationDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdReservation.id())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toModel(createdReservation));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<ReservationDTO>> update(@RequestBody ReservationDTO updatedDTO) {
        ReservationDTO result = service.update(updatedDTO);
        return ResponseEntity.ok(assembler.toModel(result));
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}