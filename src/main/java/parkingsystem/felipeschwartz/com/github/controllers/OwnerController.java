package parkingsystem.felipeschwartz.com.github.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
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
    public List<Owner> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Owner findById(@PathVariable Long id) {
        return service.findOwnerById(id);
    }

    @GetMapping(value = "/cpf/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OwnerIndividual findByCpf(@PathVariable String cpf) {
        return service.findOwnerByCpf(cpf);
    }

    @GetMapping(value = "/cnpj/{cnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    public OwnerEntity findByCnpj(@PathVariable String cnpj) {
        return service.findOwnerByCnpj(cnpj);
    }

    @PostMapping(
            value = "/entity",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public OwnerEntity createEntity(@RequestBody OwnerEntity entity) {
        return service.createEntity(entity);
    }

    @PostMapping(
            value = "/individual",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public OwnerIndividual createIndividual(@RequestBody OwnerIndividual individual) {
        return service.createIndividual(individual);
    }

    @PutMapping(
            value = "/individuals/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public OwnerIndividual updateIndividuals(@PathVariable Long id, @RequestBody OwnerIndividual updatedIndividual) {
        return service.updateIndividual(id, updatedIndividual.getPhone(), updatedIndividual.getEmail(), updatedIndividual.getCpf(), updatedIndividual.getFirstName(), updatedIndividual.getLastName(), updatedIndividual.getBirthDate(), updatedIndividual.getAddress());
    }

    @PutMapping(
            value = "/entities/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public OwnerEntity updateEntities(@PathVariable Long id, @RequestBody OwnerEntity updatedEntity) {
        return service.updateEntity(id, updatedEntity.getPhone(), updatedEntity.getEmail(),updatedEntity.getCnpj(), updatedEntity.getBillingContact(), updatedEntity.getCorporateName(), updatedEntity.getFantasyName(), updatedEntity.getAddress());
    }

    @DeleteMapping(value = "/id/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}