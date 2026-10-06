package com.github.felipeschwartz.parkingsystem.service;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FixedWindowRateLimiterTest {

    private final AuthRateLimiterTest.MutableClock clock =
            new AuthRateLimiterTest.MutableClock(Instant.parse("2026-01-01T10:00:00Z"));
    private final FixedWindowRateLimiter limiter = new FixedWindowRateLimiter(3, Duration.ofMinutes(15), clock);

    @Test
    void shouldDropExpiredKeysOnceThereAreTooManyTracked() {
        for (int i = 0; i <= 10_000; i++) {
            limiter.recordAttempt("key-" + i);
        }
        assertTrue(limiter.trackedKeys() > 10_000);

        clock.advance(Duration.ofMinutes(16));
        limiter.recordAttempt("fresh");

        assertEquals(1, limiter.trackedKeys());
    }

    @Test
    void shouldKeepActiveKeysDuringCleanup() {
        for (int i = 0; i <= 10_000; i++) {
            limiter.recordAttempt("old-" + i);
        }
        clock.advance(Duration.ofMinutes(10));
        limiter.recordAttempt("recent");
        clock.advance(Duration.ofMinutes(6));
        limiter.recordAttempt("recent");
        limiter.recordAttempt("recent");

        assertEquals(1, limiter.trackedKeys());
        assertTrue(limiter.secondsUntilAllowed("recent") > 0);
    }

    @Test
    void shouldSweepAtMostOncePerMinute() {
        for (int i = 0; i <= 10_000; i++) {
            limiter.recordAttempt("old-" + i);
        }
        clock.advance(Duration.ofSeconds(14 * 60 + 30));
        limiter.recordAttempt("b");

        clock.advance(Duration.ofSeconds(30));
        limiter.recordAttempt("c");
        assertEquals(10_003, limiter.trackedKeys(), "old keys are expired, but the last sweep was only 30s ago");

        clock.advance(Duration.ofMinutes(1));
        limiter.recordAttempt("d");
        assertEquals(3, limiter.trackedKeys());
    }
}
