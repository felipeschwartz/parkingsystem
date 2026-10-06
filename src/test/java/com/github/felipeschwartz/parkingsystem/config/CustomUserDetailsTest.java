package com.github.felipeschwartz.parkingsystem.config;

import com.github.felipeschwartz.parkingsystem.model.entity.UserEntity;
import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    void shouldJoinFirstAndLastNameForIndividuals() {
        UserIndividual user = individual("Ana", "Silva");

        assertEquals("Ana Silva", new CustomUserDetails(user).getName());
    }

    @Test
    void shouldNotPrintNullWhenPartOfTheNameIsMissing() {
        assertEquals("Ana", new CustomUserDetails(individual("Ana", null)).getName());
        assertEquals("Silva", new CustomUserDetails(individual(" ", "Silva")).getName());
    }

    @Test
    void shouldFallBackToEmailWhenThereIsNoName() {
        assertEquals("ana@teste.com", new CustomUserDetails(individual(null, null)).getName());
    }

    @Test
    void shouldUseCorporateNameForEntities() {
        UserEntity user = new UserEntity();
        user.setEmail("alpha@teste.com");
        user.setPassword("encoded");
        user.setCnpj("11111111000111");
        user.setCorporateName("Empresa Alpha LTDA");
        user.setRoles(Set.of("ROLE_USER"));

        CustomUserDetails details = new CustomUserDetails(user);

        assertEquals("Empresa Alpha LTDA", details.getName());
        assertEquals("11111111000111", details.getCnpj());
        assertNull(details.getCpf());
    }

    @Test
    void shouldNotFollowLaterChangesToTheUserRoles() {
        UserIndividual user = individual("Ana", "Silva");
        Set<String> mutableRoles = new HashSet<>(Set.of("ROLE_USER"));
        user.setRoles(mutableRoles);

        CustomUserDetails details = new CustomUserDetails(user);
        mutableRoles.add("ROLE_ADMIN");

        assertEquals(Set.of("ROLE_USER"), details.getRoles());
        assertEquals(1, details.getAuthorities().size());
    }

    @Test
    void shouldExposeUnmodifiableRolesAndAuthorities() {
        CustomUserDetails details = new CustomUserDetails(individual("Ana", "Silva"));

        assertThrows(UnsupportedOperationException.class, () -> details.getRoles().add("ROLE_ADMIN"));
        assertThrows(UnsupportedOperationException.class, () -> details.getAuthorities().clear());
    }

    private UserIndividual individual(String firstName, String lastName) {
        UserIndividual user = new UserIndividual();
        user.setId(7L);
        user.setEmail("ana@teste.com");
        user.setPassword("encoded");
        user.setCpf("11111111111");
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRoles(Set.of("ROLE_USER"));
        return user;
    }
}
