package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.ParkingLotModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.ParkingLotDTO;
import com.github.felipeschwartz.parkingsystem.service.ParkingLotService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ParkingLotControllerTest {

    private final ParkingLotService service = mock(ParkingLotService.class);
    private final MockMvc mvc = StandaloneMvc.of(new ParkingLotController(service, new ParkingLotModelAssembler()));
    private final ParkingLotDTO lot = TestDtos.parkingLot(1L);


    @Test
    void findAllShouldReturnTheLots() throws Exception {
        when(service.findAll()).thenReturn(List.of(lot));

        mvc.perform(get("/api/parking_lot/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findByIdShouldReturnTheLot() throws Exception {
        when(service.findById(1L)).thenReturn(lot);

        mvc.perform(get("/api/parking_lot/v1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createShouldReturn201WithLocation() throws Exception {
        when(service.create(any(ParkingLotDTO.class))).thenReturn(lot);

        mvc.perform(post("/api/parking_lot/v1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/parking_lot/v1/1")));
    }

    @Test
    void updateShouldReturnTheUpdatedLot() throws Exception {
        when(service.update(any(ParkingLotDTO.class))).thenReturn(lot);

        mvc.perform(put("/api/parking_lot/v1/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mvc.perform(delete("/api/parking_lot/v1/id/1")).andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}
