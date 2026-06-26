package com.github.felipeschwartz.parkingsystem.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.felipeschwartz.parkingsystem.model.dto.OwnerDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.OwnerEntityDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.OwnerIndividualDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.OwnerIndividual;
import com.github.felipeschwartz.parkingsystem.service.OwnerService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService service;

    public OwnerController(OwnerService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OwnerDTO>> findAll() {
        List<OwnerDTO> owners = service.findAll();
        return ResponseEntity.ok(owners);
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OwnerDTO> findById(@PathVariable Long id) {
        return service.findOwnerById(id)
                .map(ownerDTO -> new ResponseEntity<>(ownerDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OwnerIndividualDTO> findByCpf(@PathVariable String cpf) {
        return service.findOwnerByCpf(cpf)
                .map(ownerIndividualDTO -> new ResponseEntity<>(ownerIndividualDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(value = "/cnpj/{cnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OwnerEntityDTO> findByCnpj(@PathVariable String cnpj) {
        return service.findOwnerByCnpj(cnpj)
                .map(ownerEntityDTO -> new ResponseEntity<>(ownerEntityDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(
            value = "/entity",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OwnerEntityDTO> createEntity(@RequestBody @Valid OwnerEntity entity) {
        OwnerEntityDTO createdEntity = service.createEntity(entity);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdEntity.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdEntity);
    }

    @PostMapping(
            value = "/individual",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OwnerIndividualDTO> createIndividual(@RequestBody @Valid OwnerIndividual individual) {
        OwnerIndividualDTO createdIndividual = service.createIndividual(individual);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}") // CORRIGIDO: Adicionado o fechamento da chave
                .buildAndExpand(createdIndividual.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdIndividual);
    }

    @PutMapping(
            value = "/individuals/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OwnerIndividualDTO> updateIndividuals(@PathVariable Long id, @RequestBody @Valid OwnerIndividualDTO updatedIndividual) {
        Optional<OwnerIndividualDTO> updatedOwner = service.updateIndividual(id, updatedIndividual);
        return updatedOwner.map(ownerIndividualDTO -> new ResponseEntity<>(ownerIndividualDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(
            value = "/entities/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OwnerEntityDTO> updateEntities(@PathVariable Long id, @RequestBody @Valid OwnerEntityDTO updatedEntity) {
        Optional<OwnerEntityDTO> updatedOwner = service.updateEntity(id, updatedEntity);
        return updatedOwner.map(ownerEntityDTO -> new ResponseEntity<>(ownerEntityDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}