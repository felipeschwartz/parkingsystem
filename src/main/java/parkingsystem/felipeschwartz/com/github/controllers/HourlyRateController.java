package parkingsystem.felipeschwartz.com.github.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkingsystem.felipeschwartz.com.github.model.entities.HourlyRate;
import parkingsystem.felipeschwartz.com.github.model.enums.VehicleType;
import parkingsystem.felipeschwartz.com.github.services.HourlyRateService;

import java.util.List;

@RestController
@RequestMapping("/hourly_rate")
public class HourlyRateController {

    @Autowired
    private HourlyRateService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HourlyRate> findAll() {
        return service.findAll();
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
