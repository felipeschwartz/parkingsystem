package parkingsystem.felipeschwartz.com.github.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerEntity;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerIndividual;
import parkingsystem.felipeschwartz.com.github.model.entities.Vehicle;
import parkingsystem.felipeschwartz.com.github.services.OwnerService;
import parkingsystem.felipeschwartz.com.github.services.VehicleService;

import java.util.List;

@RestController
@RequestMapping("/vehicle")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vehicle> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Vehicle findById(@PathVariable Long id) {
        return service.findById(id);
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Vehicle> create(@RequestBody @Valid Vehicle vehicle) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(vehicle));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Vehicle update(@PathVariable Long id, @RequestBody @Valid Vehicle updatedVehicle) {
        return service.update(id, updatedVehicle);
    }


    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    
}