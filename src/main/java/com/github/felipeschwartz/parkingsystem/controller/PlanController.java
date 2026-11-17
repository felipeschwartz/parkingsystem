package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.model.dto.PagedResponse;
import com.github.felipeschwartz.parkingsystem.controller.assembler.PlanModelAssembler;
import org.springframework.hateoas.EntityModel;
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

@RestController
@RequestMapping("/api/plan/v1")
@Tag(name = "Plan", description = "Endpoint for managing Plans")
public class PlanController implements PlanControllerDocs {

    private final PlanService service;
    private final PlanModelAssembler assembler;

    public PlanController(PlanService service, PlanModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public PagedResponse<EntityModel<PlanDTO>> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        return PagedResponse.from(service.findAll(page, pageSize), assembler::toModel);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<PlanDTO> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<PlanDTO>> create(@RequestBody @Valid PlanCreationDTO planCreationDTO) {
        PlanDTO createdPlan = service.create(planCreationDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPlan.id())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toModel(createdPlan));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public EntityModel<PlanDTO> update(@RequestBody @Valid PlanDTO updatedPlan) {
        return assembler.toModel(service.update(updatedPlan));
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