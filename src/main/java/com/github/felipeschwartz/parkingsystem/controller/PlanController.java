package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.docs.PlanControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanCreationDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Plan;
import com.github.felipeschwartz.parkingsystem.service.PlanService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/plan/v1")
@Tag(name = "Plan", description = "Endpoint for managing Plans")
public class PlanController implements PlanControllerDocs {

    private final PlanService service;

    public PlanController(PlanService service) {
        this.service = service;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<PlanDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public PlanDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<PlanDTO> create(@RequestBody @Valid PlanCreationDTO planCreationDTO) {
        PlanDTO createdPlan = service.create(planCreationDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPlan.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdPlan);
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public PlanDTO update(@RequestBody @Valid PlanDTO updatedPlan) {
        return service.update(updatedPlan);
    }

    @PutMapping(value = "/activate/{id}")
    @Override
    public ResponseEntity<?> activatePlan(@PathVariable Long id) {
        service.activatePlan(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/deactivate/{id}")
    @Override
    public ResponseEntity<?> deactivatePlan(@PathVariable Long id) {
        service.deactivatePlan(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.deletePlan(id);
        return ResponseEntity.noContent().build();
    }

}