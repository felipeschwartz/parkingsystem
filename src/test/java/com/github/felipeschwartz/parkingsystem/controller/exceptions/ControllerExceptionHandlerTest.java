package com.github.felipeschwartz.parkingsystem.controller.exceptions;

import com.github.felipeschwartz.parkingsystem.config.JwtService;
import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ControllerExceptionHandlerTest.ThrowingController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ControllerExceptionHandlerTest.ThrowingController.class)
@MockitoBean(types = {JwtService.class, UserDetailsService.class})
class ControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void shouldReturn400ForIllegalArgument() throws Exception {
        mvc.perform(get("/probe/illegal-argument"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("endDate cannot be earlier than startDate."))
                .andExpect(jsonPath("$.path").value("/probe/illegal-argument"));
    }

    @Test
    void shouldReturn409ForIllegalState() throws Exception {
        mvc.perform(get("/probe/illegal-state"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Only OPEN sessions can be closed."));
    }

    @Test
    void shouldKeepReturning404ForObjectNotFound() throws Exception {
        mvc.perform(get("/probe/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Vehicle not found by ID: 9"));
    }

    @Test
    void shouldReturnTheMessageOfAnObjectNotFoundCreatedFromASingleString() throws Exception {
        mvc.perform(get("/probe/not-found-message"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Plan with id 3 not found!"));
    }

    @Test
    void shouldListEveryInvalidFieldOfTheBody() throws Exception {
        mvc.perform(post("/probe/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"code\":\"ABCDEFGHIJK\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.errors.length()").value(2))
                .andExpect(jsonPath("$.errors[?(@.field=='name')].message").value("Name cannot be blank"))
                .andExpect(jsonPath("$.errors[?(@.field=='code')].message").value("Code must not exceed 5 characters"));
    }

    @Test
    void shouldReturn400WithTheParameterNameForConstraintViolations() throws Exception {
        mvc.perform(get("/probe/constraint").param("quantity", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed."))
                .andExpect(jsonPath("$.errors[0].field").value("quantity"))
                .andExpect(jsonPath("$.errors[0].message").value("Quantity must be at least 1"));
    }

    @Test
    void shouldReturn400ForMalformedJsonWithoutLeakingParserDetails() throws Exception {
        mvc.perform(post("/probe/body")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed or unreadable request body."));
    }

    @Test
    void shouldReturn400WhenPathVariableHasTheWrongType() throws Exception {
        mvc.perform(get("/probe/by-id/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid value for parameter 'id'."));
    }

    record ProbeBody(
            @NotBlank(message = "Name cannot be blank") String name,
            @Size(max = 5, message = "Code must not exceed 5 characters") String code
    ) {
    }

    @RestController
    @RequestMapping("/probe")
    @Validated
    static class ThrowingController {

        @GetMapping("/illegal-argument")
        String illegalArgument() {
            throw new IllegalArgumentException("endDate cannot be earlier than startDate.");
        }

        @GetMapping("/illegal-state")
        String illegalState() {
            throw new IllegalStateException("Only OPEN sessions can be closed.");
        }

        @GetMapping("/not-found")
        String notFound() {
            throw new ObjectNotFoundException("Vehicle", 9L);
        }

        @GetMapping("/not-found-message")
        String notFoundWithMessage() {
            throw new ObjectNotFoundException("Plan with id 3 not found!");
        }

        @PostMapping("/body")
        String body(@RequestBody @Valid ProbeBody body) {
            return "ok";
        }

        @GetMapping("/constraint")
        String constraint(@RequestParam @Min(value = 1, message = "Quantity must be at least 1") int quantity) {
            return "ok";
        }

        @GetMapping("/by-id/{id}")
        String byId(@PathVariable Long id) {
            return "ok";
        }
    }
}
