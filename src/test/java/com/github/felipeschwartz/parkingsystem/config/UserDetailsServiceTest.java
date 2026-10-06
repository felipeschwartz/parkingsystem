package com.github.felipeschwartz.parkingsystem.config;

import com.github.felipeschwartz.parkingsystem.model.entity.UserEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import com.github.felipeschwartz.parkingsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserDetailsServiceTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserDetailsService service = new UserDetailsService(userRepository);

    @Test
    void shouldReturnCustomUserDetailsWithIdAndCpfForIndividual() {
        UserIndividual user = new UserIndividual();
        user.setId(7L);
        user.setEmail("ana@teste.com");
        user.setPassword("encoded");
        user.setCpf("11111111111");
        user.setRoles(Set.of("ROLE_USER"));
        when(userRepository.findByEmail("ana@teste.com")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("ana@teste.com");

        CustomUserDetails custom = assertInstanceOf(CustomUserDetails.class, details);
        assertEquals(7L, custom.getId());
        assertEquals("11111111111", custom.getCpf());
        assertNull(custom.getCnpj());
        assertEquals("ana@teste.com", custom.getUsername());
        assertTrue(custom.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void shouldReturnCustomUserDetailsWithCnpjForEntity() {
        UserEntity user = new UserEntity();
        user.setId(9L);
        user.setEmail("alpha@teste.com");
        user.setPassword("encoded");
        user.setCnpj("11111111000111");
        user.setRoles(Set.of("ROLE_USER"));
        when(userRepository.findByEmail("alpha@teste.com")).thenReturn(Optional.of(user));

        CustomUserDetails custom = assertInstanceOf(CustomUserDetails.class, service.loadUserByUsername("alpha@teste.com"));

        assertEquals(9L, custom.getId());
        assertEquals("11111111000111", custom.getCnpj());
        assertNull(custom.getCpf());
    }

    @Test
    void principalShouldSatisfyPreAuthorizeSelfAccessExpressions() {
        UserIndividual user = new UserIndividual();
        user.setId(7L);
        user.setEmail("ana@teste.com");
        user.setPassword("encoded");
        user.setCpf("11111111111");
        user.setRoles(Set.of("ROLE_USER"));
        when(userRepository.findByEmail("ana@teste.com")).thenReturn(Optional.of(user));
        UserDetails details = service.loadUserByUsername("ana@teste.com");

        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setVariable("id", 7L);
        ctx.setVariable("cpf", "11111111111");
        ctx.setRootObject(new Root(new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities())));

        SpelExpressionParser parser = new SpelExpressionParser();
        assertEquals(true, parser.parseExpression("#id == authentication.principal.id").getValue(ctx, Boolean.class));
        assertEquals(true, parser.parseExpression("#cpf == authentication.principal.cpf").getValue(ctx, Boolean.class));
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("x@teste.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("x@teste.com"));
    }

    public record Root(org.springframework.security.core.Authentication authentication) {
        public org.springframework.security.core.Authentication getAuthentication() {
            return authentication;
        }
    }
}
