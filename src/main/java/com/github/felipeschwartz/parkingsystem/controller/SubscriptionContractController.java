package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import com.github.felipeschwartz.parkingsystem.service.SubscriptionContractService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/contracts")
public class SubscriptionContractController {

    private final SubscriptionContractService service;

    public SubscriptionContractController(SubscriptionContractService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SubscriptionContractDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SubscriptionContractDTO findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubscriptionContractDTO> create(@RequestBody @Valid SubscriptionContractDTO subscriptionContractDTO) {
        SubscriptionContractDTO createdSubscription = service.create(subscriptionContractDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdSubscription.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdSubscription);
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubscriptionContractDTO> update(@PathVariable Long id, @RequestBody @Valid SubscriptionContractDTO subscriptionContractDTO) {
        subscriptionContractDTO.setId(id);
        SubscriptionContractDTO updatedSubscription = service.update(subscriptionContractDTO);
        return ResponseEntity.ok(updatedSubscription);
    }

    @PutMapping(
            value = "/renew/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<SubscriptionContractDTO> renew(@PathVariable Long id, @RequestBody SubscriptionContractDTO subscriptionContractDTO) {
        subscriptionContractDTO.setId(id);
        SubscriptionContractDTO renewed = service.renew(subscriptionContractDTO);
        return ResponseEntity.ok(renewed);
    }



    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
