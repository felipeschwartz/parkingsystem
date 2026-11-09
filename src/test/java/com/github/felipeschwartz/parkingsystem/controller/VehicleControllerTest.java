package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.TestDtos;
import com.github.felipeschwartz.parkingsystem.controller.assembler.VehicleModelAssembler;
import com.github.felipeschwartz.parkingsystem.model.dto.VehicleDTO;
import com.github.felipeschwartz.parkingsystem.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VehicleControllerTest {

    private final VehicleService service = mock(VehicleService.class);
    private final MockMvc mvc = StandaloneMvc.of(new VehicleController(service, new VehicleModelAssembler()));
    private final VehicleDTO vehicle = TestDtos.vehicle(1L, "ABC1234");


    @Test
    void findAllShouldReturnTheVehicles() throws Exception {
        when(service.findAll()).thenReturn(List.of(vehicle));

        mvc.perform(get("/api/vehicle/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void findByIdShouldReturnTheVehicle() throws Exception {
        when(service.findById(1L)).thenReturn(vehicle);

        mvc.perform(get("/api/vehicle/v1/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void findByLicensePlateShouldReturnTheVehicle() throws Exception {
        when(service.findByLicensePlate("ABC1234")).thenReturn(vehicle);

        mvc.perform(get("/api/vehicle/v1/licence_plate/ABC1234"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licensePlate").value("ABC1234"));
    }

    @Test
    void createShouldReturn201WithLocation() throws Exception {
        when(service.create(any(VehicleDTO.class))).thenReturn(vehicle);

        mvc.perform(post("/api/vehicle/v1").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"licensePlate\":\"ABC1234\"}"))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/vehicle/v1/id/1")));
    }

    @Test
    void updateShouldReturnTheUpdatedVehicle() throws Exception {
        when(service.update(any(VehicleDTO.class))).thenReturn(vehicle);

        mvc.perform(put("/api/vehicle/v1/1").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        mvc.perform(delete("/api/vehicle/v1/1")).andExpect(status().isNoContent());

        verify(service).delete(1L);
    }
}
