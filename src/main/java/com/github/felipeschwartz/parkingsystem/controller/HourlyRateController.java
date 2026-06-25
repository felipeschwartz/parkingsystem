package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.felipeschwartz.parkingsystem.model.entity.HourlyRate;
import com.github.felipeschwartz.parkingsystem.service.HourlyRateService;

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
    public HourlyRate findById(@PathVariable("id") long id) {
        return service.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public HourlyRate create(@RequestBody HourlyRate hourlyRate) {
        return service.create(hourlyRate);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public HourlyRate update(@RequestBody HourlyRate hourlyRate) {
        return service.update(hourlyRate);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
