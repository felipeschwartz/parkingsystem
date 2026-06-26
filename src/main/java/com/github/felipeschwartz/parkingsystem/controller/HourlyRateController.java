package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO;
import com.github.felipeschwartz.parkingsystem.service.HourlyRateService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/hourly_rate")
public class HourlyRateController {

    private final HourlyRateService service;

    public HourlyRateController(HourlyRateService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<HourlyRateDTO>> findAll() {
        List<HourlyRateDTO> hourlyRates = service.findAll();
        return ResponseEntity.ok(hourlyRates);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HourlyRateDTO> findById(@PathVariable("id") long id) {
        HourlyRateDTO hourlyRate = service.findById(id);
        return ResponseEntity.ok(hourlyRate);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HourlyRateDTO> create(@RequestBody @Valid HourlyRateDTO hourlyRateDTO) {
        HourlyRateDTO createdHourlyRate = service.create(hourlyRateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdHourlyRate.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdHourlyRate);
    }

    @PutMapping(
            value = "/{id}", // Adicionado path variable para o ID
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<HourlyRateDTO> update(@PathVariable Long id, @RequestBody @Valid HourlyRateDTO hourlyRateDTO) {
        HourlyRateDTO updatedHourlyRate = service.update(id, hourlyRateDTO);
        return ResponseEntity.ok(updatedHourlyRate);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        service.delete(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}