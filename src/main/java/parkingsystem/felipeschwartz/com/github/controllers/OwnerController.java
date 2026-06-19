package parkingsystem.felipeschwartz.com.github.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerDTO;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerEntityDTO;
import parkingsystem.felipeschwartz.com.github.data.dto.OwnerIndividualDTO;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerEntity;
import parkingsystem.felipeschwartz.com.github.model.entities.OwnerIndividual;
import parkingsystem.felipeschwartz.com.github.services.OwnerService;

import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private final OwnerService service;

    public OwnerController(OwnerService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OwnerDTO> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OwnerDTO findById(@PathVariable Long id) {
        return service.findOwnerById(id);
    }

    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OwnerIndividualDTO findByCpf(@PathVariable String cpf) {
        return service.findOwnerByCpf(cpf);
    }

    @GetMapping(value = "/cnpj/{cnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OwnerEntityDTO findByCnpj(@PathVariable String cnpj) {
        return service.findOwnerByCnpj(cnpj);
    }

    @PostMapping(
            value = "/entity",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OwnerEntityDTO> createEntity(@RequestBody @Valid OwnerEntity entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createEntity(entity));
    }

    @PostMapping(
            value = "/individual",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<OwnerIndividualDTO> createIndividual(@RequestBody @Valid OwnerIndividual individual) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createIndividual(individual));
    }

    @PutMapping(
            value = "/individuals/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public OwnerIndividualDTO updateIndividuals(@PathVariable Long id, @RequestBody @Valid OwnerIndividual updatedIndividual) {
        return service.updateIndividual(id, updatedIndividual);
    }

    @PutMapping(
            value = "/entities/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public OwnerEntityDTO updateEntities(@PathVariable Long id, @RequestBody @Valid OwnerEntity updatedEntity) {
        return service.updateEntity(id, updatedEntity);
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}