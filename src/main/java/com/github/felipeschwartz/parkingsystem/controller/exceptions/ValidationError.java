package com.github.felipeschwartz.parkingsystem.controller.exceptions;

import java.util.List;

public class ValidationError extends StandardError {
    private static final long serialVersionUID = 1L;

    public record FieldMessage(String field, String message) implements java.io.Serializable {
    }

    private final List<FieldMessage> errors;

    public ValidationError(Long timestamp, Integer status, String error, String message, String path, List<FieldMessage> errors) {
        super(timestamp, status, error, message, path);
        this.errors = errors;
    }

    public List<FieldMessage> getErrors() {
        return errors;
    }
}
