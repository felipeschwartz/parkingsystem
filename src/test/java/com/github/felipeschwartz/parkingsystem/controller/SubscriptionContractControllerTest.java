package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.SubscriptionContractModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.SubscriptionContractDTO;
import com.github.felipeschwartz.parkingsystem.service.SubscriptionContractService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SubscriptionContractControllerTest {

    private final SubscriptionContractService service = mock(SubscriptionContractService.class);
    private final MockMvc mvc = StandaloneMvc.of(new SubscriptionContractController(service, new SubscriptionContractModelAssembler()));
    private final SubscriptionContractDTO contract = TestDtos.subscriptionContract(1L);


    @Test
    void findAllShouldReturnTheContracts() throws Exception {
        when(service.findAll()).thenReturn(List.of(contract));

        mvc.perform(get("/api/contracts/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findByIdShouldReturnTheContract() throws Exception {
        when(service.findById(1L)).thenReturn(contract);

        mvc.perform(get("/api/contracts/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createShouldReturn201WithLocation() throws Exception {
        when(service.create(any(SubscriptionContractDTO.class))).thenReturn(contract);

        mvc.perform(post("/api/contracts/v1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/contracts/v1/1")));
    }

    @Test
    void updateShouldReturnTheUpdatedContract() throws Exception {
        when(service.update(any(SubscriptionContractDTO.class))).thenReturn(contract);

        mvc.perform(put("/api/contracts/v1/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void renewShouldTakeTheIdFromThePath() throws Exception {
        when(service.renew(eq(1L), any(SubscriptionContractDTO.class))).thenReturn(contract);

        mvc.perform(put("/api/contracts/v1/renew/1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":99,\"endDate\":\"2030-01-01\"}"))
                .andExpect(status().isOk());

        ArgumentCaptor<SubscriptionContractDTO> sent = ArgumentCaptor.forClass(SubscriptionContractDTO.class);
        verify(service).renew(eq(1L), sent.capture());
        assertEquals(LocalDate.of(2030, 1, 1), sent.getValue().endDate());
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mvc.perform(delete("/api/contracts/v1/1")).andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}
