package com.github.felipeschwartz.parkingsystem.controller;

import com.github.felipeschwartz.parkingsystem.config.CustomUserDetails;
import com.github.felipeschwartz.parkingsystem.config.JwtFilter;
import com.github.felipeschwartz.parkingsystem.config.JwtService;
import com.github.felipeschwartz.parkingsystem.config.SecurityConfigDev;
import com.github.felipeschwartz.parkingsystem.config.UserDetailsService;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import com.github.felipeschwartz.parkingsystem.service.AuthRateLimiter;
import com.github.felipeschwartz.parkingsystem.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ActiveProfiles("dev")
@Import({SecurityConfigDev.class, JwtFilter.class, AuthService.class, AuthRateLimiter.class})
@MockitoBean(types = {JwtService.class, UserDetailsService.class})
class AuthControllerTest {

    private static final AtomicInteger NEXT_IP = new AtomicInteger();

    private String ip;

    @BeforeEach
    void useFreshClientIp() {
        ip = "10.0." + (NEXT_IP.incrementAndGet() / 250) + "." + (NEXT_IP.get() % 250 + 1);
    }

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldReturn401WhenEmailDoesNotExist() throws Exception {
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("not found"));

        login("ghost@teste.com", "whatever")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Invalid email or password."))
                .andExpect(jsonPath("$.path").value("/auth/login"));
    }

    @Test
    void shouldReturn401WithSameBodyWhenPasswordIsWrong() throws Exception {
        registerUser("wrong@teste.com", "right-password");

        login("wrong@teste.com", "bad-password")
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password."));
    }

    @Test
    void shouldReturnTokenAndUserSummaryOnSuccess() throws Exception {
        registerUser("ana@teste.com", "right-password");
        when(jwtService.generateToken("ana@teste.com")).thenReturn("jwt-token");

        login("ana@teste.com", "right-password")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.user.id").value(7))
                .andExpect(jsonPath("$.user.name").value("Ana Silva"))
                .andExpect(jsonPath("$.user.email").value("ana@teste.com"))
                .andExpect(jsonPath("$.user.roles[0]").value("ROLE_USER"));
    }

    @Test
    void shouldReturn429WithRetryAfterAfterFiveFailures() throws Exception {
        registerUser("blocked@teste.com", "right-password");

        for (int i = 0; i < 5; i++) {
            login("blocked@teste.com", "bad-password").andExpect(status().isUnauthorized());
        }

        String retryAfter = login("blocked@teste.com", "bad-password")
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.status").value(429))
                .andExpect(jsonPath("$.error").value("Too Many Requests"))
                .andExpect(header().exists("Retry-After"))
                .andReturn().getResponse().getHeader("Retry-After");

        long seconds = Long.parseLong(retryAfter);
        assertTrue(seconds > 0 && seconds <= 15 * 60, "Retry-After should be within the 15 minute window, was " + seconds);
    }

    @Test
    void shouldKeepBlockingEvenWithCorrectPasswordWhileLimited() throws Exception {
        registerUser("locked@teste.com", "right-password");
        for (int i = 0; i < 5; i++) {
            login("locked@teste.com", "bad-password");
        }

        login("locked@teste.com", "right-password").andExpect(status().isTooManyRequests());
    }

    @Test
    void shouldResetFailureCounterAfterSuccessfulLogin() throws Exception {
        registerUser("reset@teste.com", "right-password");
        when(jwtService.generateToken("reset@teste.com")).thenReturn("jwt-token");

        for (int i = 0; i < 4; i++) {
            login("reset@teste.com", "bad-password").andExpect(status().isUnauthorized());
        }
        login("reset@teste.com", "right-password").andExpect(status().isOk());

        for (int i = 0; i < 4; i++) {
            login("reset@teste.com", "bad-password").andExpect(status().isUnauthorized());
        }
    }

    @Test
    void shouldCountFailuresPerEmail() throws Exception {
        registerUser("victim@teste.com", "right-password");
        registerUser("other@teste.com", "right-password");
        when(jwtService.generateToken("other@teste.com")).thenReturn("jwt-token");

        for (int i = 0; i < 5; i++) {
            login("victim@teste.com", "bad-password");
        }

        login("other@teste.com", "right-password").andExpect(status().isOk());
    }

    @Test
    void shouldBlockAnIpThatTriesManyDifferentEmailsWithoutAffectingOtherIps() throws Exception {
        when(userDetailsService.loadUserByUsername(anyString())).thenThrow(new UsernameNotFoundException("not found"));
        registerUser("real@teste.com", "right-password");
        when(jwtService.generateToken("real@teste.com")).thenReturn("jwt-token");
        String sprayingIp = "192.168.50.1";

        for (int i = 0; i < 20; i++) {
            login("spray" + i + "@teste.com", "bad-password", sprayingIp).andExpect(status().isUnauthorized());
        }

        login("spray-new@teste.com", "bad-password", sprayingIp).andExpect(status().isTooManyRequests());
        login("real@teste.com", "right-password", sprayingIp).andExpect(status().isTooManyRequests());
        login("real@teste.com", "right-password", "192.168.50.2").andExpect(status().isOk());
    }

    private ResultActions login(String email, String password) throws Exception {
        return login(email, password, ip);
    }

    private ResultActions login(String email, String password, String remoteAddr) throws Exception {
        return mvc.perform(post("/auth/login")
                .with(request -> {
                    request.setRemoteAddr(remoteAddr);
                    return request;
                })
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}"));
    }

    private void registerUser(String email, String rawPassword) {
        UserIndividual user = new UserIndividual();
        user.setId(7L);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setCpf("11111111111");
        user.setFirstName("Ana");
        user.setLastName("Silva");
        user.setRoles(Set.of("ROLE_USER"));
        doReturn(new CustomUserDetails(user)).when(userDetailsService).loadUserByUsername(email);
    }
}
