package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.docs.HourlyRateControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO;
import com.github.felipeschwartz.parkingsystem.service.HourlyRateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/hourly_rate/v1")
@Tag(name = "Hourly Rate", description = "Endpoints for managing Hourly Rate")
public class HourlyRateController implements HourlyRateControllerDocs {

    private final HourlyRateService service;

    public HourlyRateController(HourlyRateService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<List<HourlyRateDTO>> findAll() {
        List<HourlyRateDTO> hourlyRates = service.findAll();
        return ResponseEntity.ok(hourlyRates);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<HourlyRateDTO> findById(@PathVariable("id") long id) {
        HourlyRateDTO hourlyRate = service.findById(id);
        return ResponseEntity.ok(hourlyRate);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<HourlyRateDTO> create(@RequestBody @Valid HourlyRateDTO hourlyRateDTO) {
        HourlyRateDTO createdHourlyRate = service.create(hourlyRateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdHourlyRate.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdHourlyRate);
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<HourlyRateDTO> update(@RequestBody @Valid HourlyRateDTO hourlyRateDTO) {
        HourlyRateDTO updatedHourlyRate = service.update(hourlyRateDTO);
        return ResponseEntity.ok(updatedHourlyRate);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}