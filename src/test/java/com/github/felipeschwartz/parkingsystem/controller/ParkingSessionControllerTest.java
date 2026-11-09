package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.ParkingSessionModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.OpenSessionRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSessionDTO;
import com.github.felipeschwartz.parkingsystem.service.ParkingSessionService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ParkingSessionControllerTest {

    private final ParkingSessionService service = mock(ParkingSessionService.class);
    private final MockMvc mvc = StandaloneMvc.of(new ParkingSessionController(service, new ParkingSessionModelAssembler()));
    private final ParkingSessionDTO session = TestDtos.parkingSession(1L);


    @Test
    void openShouldReturn201WithLocation() throws Exception {
        when(service.openParkingSession(any(OpenSessionRequestDTO.class))).thenReturn(session);

        mvc.perform(post("/api/parking_sessions/v1/open").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"parkingSpaceId\":4,\"licensePlate\":\"ABC1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/parking_sessions/v1/1")))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void closeShouldPassTheExitTimeFromTheBody() throws Exception {
        LocalDateTime exit = LocalDateTime.of(2026, 1, 1, 12, 0);
        when(service.closeSession(1L, exit)).thenReturn(session);

        mvc.perform(post("/api/parking_sessions/v1/1/close").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"exitTime\":\"2026-01-01T12:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void closeShouldAcceptARequestWithoutBody() throws Exception {
        when(service.closeSession(eq(1L), isNull())).thenReturn(session);

        mvc.perform(post("/api/parking_sessions/v1/1/close")).andExpect(status().isOk());

        verify(service).closeSession(1L, null);
    }

    @Test
    void findByIdShouldReturnTheSession() throws Exception {
        when(service.findById(1L)).thenReturn(session);

        mvc.perform(get("/api/parking_sessions/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findAllShouldReturnTheSessions() throws Exception {
        when(service.findAll()).thenReturn(List.of(session));

        mvc.perform(get("/api/parking_sessions/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findOpenSessionsByParkingSpaceShouldReturnTheSessions() throws Exception {
        when(service.findOpenSessionsByParkingSpace(4L)).thenReturn(List.of(session));

        mvc.perform(get("/api/parking_sessions/v1/open/space/4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findSessionsByVehicleShouldReturnTheSessions() throws Exception {
        when(service.findSessionsByVehicle(7L)).thenReturn(List.of(session));

        mvc.perform(get("/api/parking_sessions/v1/vehicle/7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
