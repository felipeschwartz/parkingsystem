package com.github.felipeschwartz.parkingsystem.service.exceptions;

public class TooManyAttemptsException extends RuntimeException {

    private final long retryAfterSeconds;

    public TooManyAttemptsException(long retryAfterSeconds) {
        super("Too many attempts. Try again in " + Math.max(1, (retryAfterSeconds + 59) / 60) + " minute(s).");
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public long getRetryAfterSeconds() {
        return retryAfterSeconds;
    }
}
