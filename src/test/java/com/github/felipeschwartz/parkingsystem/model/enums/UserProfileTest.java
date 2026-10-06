package com.github.felipeschwartz.parkingsystem.model.enums;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class UserProfileTest {

    @Test
    void shouldGiveEachProfileItsOwnRole() {
        assertEquals(Set.of("ROLE_USER"), UserProfile.USER.roles());
        assertEquals(Set.of("ROLE_PARKING"), UserProfile.PARKING.roles());
        assertEquals(Set.of("ROLE_PARKING_MANAGER"), UserProfile.PARKING_MANAGER.roles());
    }

    @Test
    void shouldGiveAdminsTheUserRoleToo() {
        assertEquals(Set.of("ROLE_ADMIN", "ROLE_USER"), UserProfile.ADMIN.roles());
    }

    @Test
    void shouldReturnAFreshSetEachTime() {
        assertNotSame(UserProfile.USER.roles(), UserProfile.USER.roles());
    }
}
