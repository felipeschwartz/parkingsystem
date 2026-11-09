package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.ParkingSpaceModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingSpaceDTO;
import com.github.felipeschwartz.parkingsystem.service.ParkingSpaceService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ParkingSpaceControllerTest {

    private final ParkingSpaceService service = mock(ParkingSpaceService.class);
    private final MockMvc mvc = StandaloneMvc.of(new ParkingSpaceController(service, new ParkingSpaceModelAssembler()));
    private final ParkingSpaceDTO space = TestDtos.parkingSpace(1L);


    @Test
    void findAllShouldReturnTheSpaces() throws Exception {
        when(service.findAll()).thenReturn(List.of(space));

        mvc.perform(get("/api/parking_space/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findByIdShouldReturnTheSpace() throws Exception {
        when(service.findById(1L)).thenReturn(space);

        mvc.perform(get("/api/parking_space/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createShouldReturn201WithLocation() throws Exception {
        when(service.createParkingSpace(any(ParkingSpaceDTO.class))).thenReturn(space);

        mvc.perform(post("/api/parking_space/v1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/parking_space/v1/1")));
    }

    @Test
    void updateShouldReturnTheUpdatedSpace() throws Exception {
        when(service.updateParkingSpace(any(ParkingSpaceDTO.class))).thenReturn(space);

        mvc.perform(put("/api/parking_space/v1/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mvc.perform(delete("/api/parking_space/v1/1")).andExpect(status().isNoContent());

        verify(service).deleteParkingSpace(1L);
    }
}
