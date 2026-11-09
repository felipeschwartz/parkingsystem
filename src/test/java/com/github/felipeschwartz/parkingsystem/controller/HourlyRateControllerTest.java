package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.HourlyRateModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.HourlyRateDTO;
import com.github.felipeschwartz.parkingsystem.service.HourlyRateService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HourlyRateControllerTest {

    private final HourlyRateService service = mock(HourlyRateService.class);
    private final MockMvc mvc = StandaloneMvc.of(new HourlyRateController(service, new HourlyRateModelAssembler()));
    private final HourlyRateDTO rate = TestDtos.hourlyRate(1L);

    @Test
    void findAllShouldReturnTheRates() throws Exception {
        when(service.findAll()).thenReturn(List.of(rate));

        mvc.perform(get("/api/hourly_rate/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findByIdShouldReturnTheRate() throws Exception {
        when(service.findById(1L)).thenReturn(rate);

        mvc.perform(get("/api/hourly_rate/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createShouldReturn201WithLocation() throws Exception {
        when(service.create(any(HourlyRateDTO.class))).thenReturn(rate);

        mvc.perform(post("/api/hourly_rate/v1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/hourly_rate/v1/1")));
    }

    @Test
    void updateShouldReturnTheUpdatedRate() throws Exception {
        when(service.update(any(HourlyRateDTO.class))).thenReturn(rate);

        mvc.perform(put("/api/hourly_rate/v1/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mvc.perform(delete("/api/hourly_rate/v1/1")).andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}
