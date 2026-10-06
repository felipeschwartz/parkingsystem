package com.github.felipeschwartz.parkingsystem.controller.exceptions;

import com.github.felipeschwartz.parkingsystem.service.exceptions.ObjectNotFoundException;
import com.github.felipeschwartz.parkingsystem.service.exceptions.TooManyAttemptsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError err = new StandardError(
                System.currentTimeMillis(),
                status.value(),
                "Not Found",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardError> badCredentials(BadCredentialsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        StandardError err = new StandardError(
                System.currentTimeMillis(),
                status.value(),
                "Unauthorized",
                "Invalid email or password.",
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(TooManyAttemptsException.class)
    public ResponseEntity<StandardError> tooManyAttempts(TooManyAttemptsException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.TOO_MANY_REQUESTS;
        StandardError err = new StandardError(
                System.currentTimeMillis(),
                status.value(),
                "Too Many Requests",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(status)
                .header(HttpHeaders.RETRY_AFTER, String.valueOf(e.getRetryAfterSeconds()))
                .body(err);
    }
}
