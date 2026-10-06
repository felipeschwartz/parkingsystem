package com.github.felipeschwartz.parkingsystem.controller.exceptions;

import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import com.github.felipeschwartz.parkingsystem.service.exceptions.TooManyAttemptsException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> badCredentials(BadCredentialsException e, HttpServletRequest request) {
        return error(HttpStatus.UNAUTHORIZED, "Invalid email or password.", request);
    }

    @ExceptionHandler(TooManyAttemptsException.class)
    public ResponseEntity<StandardError> tooManyAttempts(TooManyAttemptsException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header(HttpHeaders.RETRY_AFTER, String.valueOf(e.getRetryAfterSeconds()))
                .body(standardError(HttpStatus.TOO_MANY_REQUESTS, e.getMessage(), request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardError> illegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        logger.warn("Invalid argument on {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return error(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    // Os services usam IllegalStateException para regras de estado (sessão já fechada, vaga ocupada, placa duplicada).
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<StandardError> illegalState(IllegalStateException e, HttpServletRequest request) {
        logger.warn("Conflict on {} {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage());
        return error(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> invalidBody(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<ValidationError.FieldMessage> fields = e.getBindingResult().getAllErrors().stream()
                .map(objectError -> new ValidationError.FieldMessage(
                        objectError instanceof FieldError fieldError ? fieldError.getField() : objectError.getObjectName(),
                        objectError.getDefaultMessage()))
                .toList();
        return validationError(fields, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardError> constraintViolation(ConstraintViolationException e, HttpServletRequest request) {
        List<ValidationError.FieldMessage> fields = e.getConstraintViolations().stream()
                .map(violation -> new ValidationError.FieldMessage(lastNode(violation), violation.getMessage()))
                .toList();
        return validationError(fields, request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<StandardError> unreadableBody(HttpMessageNotReadableException e, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Malformed or unreadable request body.", request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<StandardError> typeMismatch(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "Invalid value for parameter '" + e.getName() + "'.", request);
    }

    private ResponseEntity<StandardError> error(HttpStatus status, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(standardError(status, message, request));
    }

    private StandardError standardError(HttpStatus status, String message, HttpServletRequest request) {
        return new StandardError(System.currentTimeMillis(), status.value(), status.getReasonPhrase(), message, request.getRequestURI());
    }

    private ResponseEntity<StandardError> validationError(List<ValidationError.FieldMessage> fields, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ValidationError body = new ValidationError(System.currentTimeMillis(), status.value(), status.getReasonPhrase(),
                "Validation failed.", request.getRequestURI(), fields);
        return ResponseEntity.status(status).body(body);
    }

    private static String lastNode(ConstraintViolation<?> violation) {
        String name = "";
        for (Path.Node node : violation.getPropertyPath()) {
            name = node.getName();
        }
        return name;
    }
}
