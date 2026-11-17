package com.github.felipeschwartz.parkingsystem.controller;

import org.springframework.data.domain.PageImpl;
import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.PaymentModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.PaymentDTO;
import com.github.felipeschwartz.parkingsystem.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest {

    private final PaymentService service = mock(PaymentService.class);
    private final MockMvc mvc = StandaloneMvc.of(new PaymentController(service, new PaymentModelAssembler()));
    private final PaymentDTO payment = TestDtos.payment(1L);


    @Test
    void findAllShouldReturnThePayments() throws Exception {
        when(service.findAll(0, 10)).thenReturn(new PageImpl<>(List.of(payment)));

        mvc.perform(get("/api/payment/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    void findByIdShouldReturnThePayment() throws Exception {
        when(service.findById(1L)).thenReturn(payment);

        mvc.perform(get("/api/payment/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createShouldReturn201WithLocation() throws Exception {
        when(service.create(any(PaymentDTO.class))).thenReturn(payment);

        mvc.perform(post("/api/payment/v1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/payment/v1/1")));
    }

    @Test
    void updateShouldReturnTheUpdatedPayment() throws Exception {
        when(service.update(any(PaymentDTO.class))).thenReturn(payment);

        mvc.perform(put("/api/payment/v1/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }
}
