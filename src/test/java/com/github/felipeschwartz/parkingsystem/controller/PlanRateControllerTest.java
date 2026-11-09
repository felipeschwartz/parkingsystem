package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.PlanRateModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanRateDTO;
import com.github.felipeschwartz.parkingsystem.service.PlanRateService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PlanRateControllerTest {

    private final PlanRateService service = mock(PlanRateService.class);
    private final MockMvc mvc = StandaloneMvc.of(new PlanRateController(service, new PlanRateModelAssembler()));
    private final PlanRateDTO rate = TestDtos.planRate(1L);


    @Test
    void findAllShouldReturnTheRates() throws Exception {
        when(service.findAll()).thenReturn(List.of(rate));

        mvc.perform(get("/api/plan_rate/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findByIdShouldReturnTheRate() throws Exception {
        when(service.findById(1L)).thenReturn(rate);

        mvc.perform(get("/api/plan_rate/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createShouldReturn201WithLocation() throws Exception {
        when(service.create(any(PlanRateDTO.class))).thenReturn(rate);

        mvc.perform(post("/api/plan_rate/v1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/plan_rate/v1/1")));
    }

    @Test
    void updateShouldReturnTheUpdatedRate() throws Exception {
        when(service.update(any(PlanRateDTO.class))).thenReturn(rate);

        mvc.perform(put("/api/plan_rate/v1/1").contentType(MediaType.APPLICATION_JSON).content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mvc.perform(delete("/api/plan_rate/v1/1")).andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    void deactivateShouldReturn204() throws Exception {
        mvc.perform(put("/api/plan_rate/v1/deactivate/1")).andExpect(status().isNoContent());

        verify(service).deactivateRate(1L);
    }

    @Test
    void activateShouldReturn204() throws Exception {
        mvc.perform(put("/api/plan_rate/v1/activate/1")).andExpect(status().isNoContent());

        verify(service).activateRate(1L);
    }
}
