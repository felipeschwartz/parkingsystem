package com.github.felipeschwartz.parkingsystem.service.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String entityName, Long id) {
        super(entityName + " not found by ID: " + id);
    }

    public ObjectNotFoundException(String entityName, String identifier) {
        super(entityName + " not found: " + identifier);
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
