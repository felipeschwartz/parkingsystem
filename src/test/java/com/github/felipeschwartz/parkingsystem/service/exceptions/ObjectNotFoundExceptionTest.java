package com.github.felipeschwartz.parkingsystem.service.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObjectNotFoundExceptionTest {

    @Test
    void shouldKeepTheMessageGivenAsASingleString() {
        assertEquals("Plan with id 3 not found!", new ObjectNotFoundException("Plan with id 3 not found!").getMessage());
    }

    @Test
    void shouldBuildTheMessageFromEntityNameAndId() {
        assertEquals("Plan not found by ID: 3", new ObjectNotFoundException("Plan", 3L).getMessage());
    }

    @Test
    void shouldBuildTheMessageFromEntityNameAndTextIdentifier() {
        assertEquals("Vehicle not found: ABC1234", new ObjectNotFoundException("Vehicle", "ABC1234").getMessage());
    }
}
