package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.assembler.SubscriptionContractModelAssembler;
import org.springframework.hateoas.EntityModel;
import com.github.felipeschwartz.parkingsystem.controller.docs.SubscriptionContractControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import com.github.felipeschwartz.parkingsystem.service.SubscriptionContractService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/contracts/v1")
@Tag(name = "Subscription Contract", description = "Endpoint for managing Subscription Contracts")
public class SubscriptionContractController implements SubscriptionContractControllerDocs {

    private final SubscriptionContractService service;
    private final SubscriptionContractModelAssembler assembler;

    public SubscriptionContractController(SubscriptionContractService service, SubscriptionContractModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<EntityModel<SubscriptionContractDTO>> findAll() {
        return service.findAll().stream().map(assembler::toModel).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<SubscriptionContractDTO> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<SubscriptionContractDTO>> create(@RequestBody @Valid SubscriptionContractDTO subscriptionContractDTO) {
        SubscriptionContractDTO createdSubscription = service.create(subscriptionContractDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSubscription.id())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toModel(createdSubscription));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<SubscriptionContractDTO>> update(@RequestBody @Valid SubscriptionContractDTO subscriptionContractDTO) {
        SubscriptionContractDTO updatedSubscription = service.update(subscriptionContractDTO);
        return ResponseEntity.ok(assembler.toModel(updatedSubscription));
    }

    @PutMapping(
            value = "/renew/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<SubscriptionContractDTO>> renew(@PathVariable Long id, @RequestBody SubscriptionContractDTO subscriptionContractDTO) {
        SubscriptionContractDTO renewed = service.renew(id, subscriptionContractDTO);
        return ResponseEntity.ok(assembler.toModel(renewed));
    }



    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
