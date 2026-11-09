package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.controller.assembler.PaymentModelAssembler;
import org.springframework.hateoas.EntityModel;
import com.github.felipeschwartz.parkingsystem.controller.docs.PaymentControllerDocs;
import com.github.felipeschwartz.parkingsystem.model.dto.PaymentDTO;
import com.github.felipeschwartz.parkingsystem.model.entity.Payment;
import com.github.felipeschwartz.parkingsystem.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/payment/v1")
@Tag(name = "Payment", description = "Endpoint for managing Payments")
public class PaymentController implements PaymentControllerDocs {

    private final PaymentService service;
    private final PaymentModelAssembler assembler;

    public PaymentController(PaymentService service, PaymentModelAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public List<EntityModel<PaymentDTO>> findAll() {
        return service.findAll().stream().map(assembler::toModel).toList();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public EntityModel<PaymentDTO> findById(@PathVariable Long id) {
        return assembler.toModel(service.findById(id));
    }


    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public ResponseEntity<EntityModel<PaymentDTO>> create(@RequestBody @Valid PaymentDTO payment) {
        PaymentDTO createdPayment = service.create(payment);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPayment.id())
                .toUri();
        return ResponseEntity.created(location).body(assembler.toModel(createdPayment));
    }

    @PutMapping(
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public EntityModel<PaymentDTO> update(@RequestBody @Valid PaymentDTO updatedPayment) {
        return assembler.toModel(service.update(updatedPayment));
    }
}