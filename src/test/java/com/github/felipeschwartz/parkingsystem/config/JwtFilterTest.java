package com.github.felipeschwartz.parkingsystem.config;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtFilterTest {

    private final JwtService jwtService = mock(JwtService.class);
    private final UserDetailsService userDetailsService = mock(UserDetailsService.class);
    private final FilterChain chain = mock(FilterChain.class);
    private final JwtFilter filter = new JwtFilter(jwtService, userDetailsService);

    private final MockHttpServletRequest request = new MockHttpServletRequest();
    private final MockHttpServletResponse response = new MockHttpServletResponse();

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldPassThroughWithoutAuthorizationHeader() throws Exception {
        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldIgnoreNonBearerSchemes() throws Exception {
        request.addHeader("Authorization", "Basic abc");

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void shouldAuthenticateWithAValidToken() throws Exception {
        UserDetails user = User.withUsername("ana@x.com").password("x").roles("ADMIN").build();
        request.addHeader("Authorization", "Bearer good");
        when(jwtService.isTokenValid("good")).thenReturn(true);
        when(jwtService.extractEmail("good")).thenReturn("ana@x.com");
        when(userDetailsService.loadUserByUsername("ana@x.com")).thenReturn(user);

        filter.doFilter(request, response, chain);

        assertSame(user, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        assertEquals(List.of("ROLE_ADMIN"), SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities().stream().map(Object::toString).toList());
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWithAnInvalidToken() throws Exception {
        request.addHeader("Authorization", "Bearer bad");
        when(jwtService.isTokenValid("bad")).thenReturn(false);

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
        verifyNoInteractions(userDetailsService);
    }

    @Test
    void shouldClearTheContextAndContinueWhenTheUserCannotBeLoaded() throws Exception {
        request.addHeader("Authorization", "Bearer good");
        when(jwtService.isTokenValid("good")).thenReturn(true);
        when(jwtService.extractEmail("good")).thenReturn("ghost@x.com");
        when(userDetailsService.loadUserByUsername("ghost@x.com")).thenThrow(new UsernameNotFoundException("no"));

        filter.doFilter(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain).doFilter(request, response);
    }
}
