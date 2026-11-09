package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.assembler.PlanRateModelAssembler;
import org.springframework.hateoas.EntityModel;
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
    private final PlanRateModelAssembler assembler;

    public PlanRateController(PlanRateService service, PlanRateModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<EntityModel<PlanRateDTO>> findAll() {
        return service.findAll().stream().map(assembler::toModel).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<PlanRateDTO> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<PlanRateDTO>> create(@RequestBody @Valid PlanRateDTO planRateDTO) {
        PlanRateDTO createdPlanRate = service.create(planRateDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPlanRate.id())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toModel(createdPlanRate));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<PlanRateDTO>> update(@RequestBody PlanRateDTO updatedDTO) {
        PlanRateDTO result = service.update(updatedDTO);
        return ResponseEntity.ok(assembler.toModel(result));
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