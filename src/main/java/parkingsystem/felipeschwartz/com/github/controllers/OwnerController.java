package parkingsystem.felipeschwartz.com.github.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.services.OwnerService;
import parkingsystem.felipeschwartz.com.github.services.OwnerService;

import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Owner> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Owner findById(@PathVariable("id") long id) {
        return service.findOwnerById(id);
    }

    @GetMapping(value = "/{cpf}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Owner findByCpf(@PathVariable("cpf") String cpf) {
        return service.findOwnerByCpf(cpf);
    }

    @GetMapping(value = "/{cnpj}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Owner findByCnpj(@PathVariable("cnpj") String cnpj) {
        return service.findOwnerByCnpj(cnpj);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Owner create(@RequestBody Owner owner) {
        return service.create(owner);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Owner update(@RequestBody Owner owner) {
        return service.update(owner.getId());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
