package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.ReservationModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.ReservationDTO;
import com.github.felipeschwartz.parkingsystem.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReservationControllerTest {

    private final ReservationService service = mock(ReservationService.class);
    private final MockMvc mvc = StandaloneMvc.of(new ReservationController(service, new ReservationModelAssembler()));
    private final ReservationDTO reservation = TestDtos.reservation(1L);


    @Test
    void findAllShouldReturnTheReservations() throws Exception {
        when(service.findAll()).thenReturn(List.of(reservation));

        mvc.perform(get("/api/reservation/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findByIdShouldReturnTheReservation() throws Exception {
        when(service.findById(1L)).thenReturn(reservation);

        mvc.perform(get("/api/reservation/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createShouldReturn201WithLocation() throws Exception {
        when(service.create(any(ReservationDTO.class))).thenReturn(reservation);

        mvc.perform(post("/api/reservation/v1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/reservation/v1/1")));
    }

    @Test
    void updateShouldReturnTheUpdatedReservation() throws Exception {
        when(service.update(any(ReservationDTO.class))).thenReturn(reservation);

        mvc.perform(put("/api/reservation/v1/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mvc.perform(delete("/api/reservation/v1/1")).andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}
