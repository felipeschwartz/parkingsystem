package com.github.felipeschwartz.parkingsystem.config;

import com.github.felipeschwartz.parkingsystem.model.entity.User;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import com.github.felipeschwartz.parkingsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminBootstrapTest {

    private static final String PASSWORD = "a-long-enough-password";

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private AdminBootstrap bootstrap(String email, String password) {
        return new AdminBootstrap(userRepository, passwordEncoder, email, password);
    }

    @Test
    void shouldDoNothingWhenNothingIsConfigured() {
        bootstrap("", "").run();

        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldFailWhenOnlyTheEmailIsConfigured() {
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> bootstrap("admin@empresa.com", "").run());

        assertEquals("ADMIN_EMAIL and ADMIN_PASSWORD must be set together.", e.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldFailWhenOnlyThePasswordIsConfigured() {
        assertThrows(IllegalStateException.class, () -> bootstrap("", PASSWORD).run());
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldFailWhenTheEmailIsNotAnEmail() {
        assertThrows(IllegalStateException.class, () -> bootstrap("not-an-email", PASSWORD).run());
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldFailWhenThePasswordIsShorterThan12Characters() {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> bootstrap("admin@empresa.com", "short-pass1").run());

        assertEquals("ADMIN_PASSWORD must have at least 12 characters.", e.getMessage());
        verifyNoInteractions(userRepository);
    }

    @Test
    void shouldNotTouchAnythingWhenAnAdminAlreadyExists() {
        when(userRepository.existsByRole("ROLE_ADMIN")).thenReturn(true);

        bootstrap("admin@empresa.com", PASSWORD).run();

        verify(userRepository, never()).save(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void shouldRefuseToPromoteAnExistingNonAdminUser() {
        when(userRepository.existsByRole("ROLE_ADMIN")).thenReturn(false);
        when(userRepository.findByEmail("ana@empresa.com")).thenReturn(Optional.of(mock(User.class)));

        assertThrows(IllegalStateException.class, () -> bootstrap("ana@empresa.com", PASSWORD).run());

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldCreateTheAdminWithAnEncodedPassword() {
        when(userRepository.existsByRole("ROLE_ADMIN")).thenReturn(false);
        when(userRepository.findByEmail("admin@empresa.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn("encoded-password");

        bootstrap("  admin@empresa.com ", PASSWORD).run();

        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(saved.capture());
        UserIndividual admin = assertInstanceOf(UserIndividual.class, saved.getValue());
        assertEquals("admin@empresa.com", admin.getEmail());
        assertEquals("encoded-password", admin.getPassword());
        assertNotEquals(PASSWORD, admin.getPassword());
        assertEquals(Set.of("ROLE_ADMIN", "ROLE_USER"), admin.getRoles());
        assertEquals(UserProfile.ADMIN, admin.getUserProfile());
        assertEquals(UserType.INDIVIDUAL, admin.getUserType());
        assertEquals(AdminBootstrap.PLACEHOLDER_CPF, admin.getCpf());
    }
}
