package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.docs.PlanRateControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanRateDTO;
import com.github.felipeschwartz.parkingsystem.service.PlanRateService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/plan_rate/v1")
@Tag(name = "Plan Rate", description = "Endpoint for managing Plan Rates")
public class PlanRateController implements PlanRateControllerDocs {

    private final PlanRateService service;

    public PlanRateController(PlanRateService service) {
        this.service = service;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<PlanRateDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public PlanRateDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<PlanRateDTO> create(@RequestBody @Valid PlanRateDTO planRateDTO) {
        PlanRateDTO createdPlanRate = service.create(planRateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPlanRate.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdPlanRate);
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<PlanRateDTO> update(@RequestBody PlanRateDTO updatedDTO) {
        updatedDTO.setId(updatedDTO.getId());
        PlanRateDTO result = service.update(updatedDTO);
        return ResponseEntity.ok(result);
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/deactivate/{id}")
    @Override
    public ResponseEntity<?> deactivatePlanRate(@PathVariable Long id) {
        service.deactivateRate(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/activate/{id}")
    @Override
    public ResponseEntity<?> activatePlanRate(@PathVariable Long id) {
        service.activateRate(id);
        return ResponseEntity.noContent().build();
    }
}