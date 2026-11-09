package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.PlanModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanCreationDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.PlanDTO;
import com.github.felipeschwartz.parkingsystem.service.PlanService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PlanControllerTest {

    private final PlanService service = mock(PlanService.class);
    private final MockMvc mvc = StandaloneMvc.of(new PlanController(service, new PlanModelAssembler()));
    private final PlanDTO plan = TestDtos.plan(1L);


    @Test
    void findAllShouldReturnThePlans() throws Exception {
        when(service.findAll()).thenReturn(List.of(plan));

        mvc.perform(get("/api/plan/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findByIdShouldReturnThePlan() throws Exception {
        when(service.findById(1L)).thenReturn(plan);

        mvc.perform(get("/api/plan/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createShouldReturn201WithLocation() throws Exception {
        when(service.create(any(PlanCreationDTO.class))).thenReturn(plan);

        mvc.perform(post("/api/plan/v1").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"Gold\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/plan/v1/1")));
    }

    @Test
    void updateShouldReturnTheUpdatedPlan() throws Exception {
        when(service.update(any(PlanDTO.class))).thenReturn(plan);

        mvc.perform(put("/api/plan/v1/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void activateShouldReturn204() throws Exception {
        mvc.perform(put("/api/plan/v1/activate/1")).andExpect(status().isNoContent());

        verify(service).activatePlan(1L);
    }

    @Test
    void deactivateShouldReturn204() throws Exception {
        mvc.perform(put("/api/plan/v1/deactivate/1")).andExpect(status().isNoContent());

        verify(service).deactivatePlan(1L);
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mvc.perform(delete("/api/plan/v1/1")).andExpect(status().isNoContent());

        verify(service).deletePlan(1L);
    }
}
