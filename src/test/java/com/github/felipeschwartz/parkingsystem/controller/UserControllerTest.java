package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.config.JwtService;
import com.github.felipeschwartz.parkingsystem.model.dto.CreateUserRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserIndividualDTO;
import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@MockitoBean(types = {JwtService.class, UserDetailsService.class})
class UserControllerTest {

    private static final String VALID_BODY = """
            {"phone":"11999999999","email":"maria@empresa.com","password":"secret-123",
             "userType":"INDIVIDUAL","userProfile":"%s","cpf":"12345678901","firstName":"Maria","lastName":"Souza"}
            """;

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService userService;

    @Test
    void shouldBindTheUserProfileSentInTheBody() throws Exception {
        UserIndividualDTO created = new UserIndividualDTO();
        created.setId(10L);
        when(userService.create(any(CreateUserRequestDTO.class))).thenReturn(created);

        mvc.perform(post("/api/user/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY.formatted("PARKING_MANAGER")))
                .andExpect(status().isCreated());

        ArgumentCaptor<CreateUserRequestDTO> sent = ArgumentCaptor.forClass(CreateUserRequestDTO.class);
        verify(userService).create(sent.capture());
        assertEquals(UserProfile.PARKING_MANAGER, sent.getValue().getUserProfile());
    }

    @Test
    void shouldRejectABodyWithoutUserProfile() throws Exception {
        mvc.perform(post("/api/user/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"phone":"11999999999","email":"maria@empresa.com","password":"secret-123",
                                 "userType":"INDIVIDUAL","cpf":"12345678901"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field=='userProfile')].message").value("User profile cannot be null"));

        verifyNoInteractions(userService);
    }
}
