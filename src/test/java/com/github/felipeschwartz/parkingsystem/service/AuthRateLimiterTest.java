package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.service.exceptions.TooManyAttemptsException;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class AuthRateLimiterTest {

    private final MutableClock clock = new MutableClock(Instant.parse("2026-01-01T10:00:00Z"));
    private final AuthRateLimiter limiter = new AuthRateLimiter(clock);

    @Test
    void shouldBlockEmailAndIpPairAfterMaxFailures() {
        failLogin("ana@teste.com", "1.1.1.1", AuthRateLimiter.MAX_LOGIN_FAILURES);

        assertThrows(TooManyAttemptsException.class, () -> limiter.checkLoginAllowed("ana@teste.com", "1.1.1.1"));
    }

    @Test
    void shouldNotBlockBeforeMaxFailures() {
        failLogin("ana@teste.com", "1.1.1.1", AuthRateLimiter.MAX_LOGIN_FAILURES - 1);

        assertDoesNotThrow(() -> limiter.checkLoginAllowed("ana@teste.com", "1.1.1.1"));
    }

    @Test
    void shouldTreatEmailCaseAndWhitespaceAsTheSameAccount() {
        failLogin("  ANA@teste.com ", "1.1.1.1", AuthRateLimiter.MAX_LOGIN_FAILURES);

        assertThrows(TooManyAttemptsException.class, () -> limiter.checkLoginAllowed("ana@teste.com", "1.1.1.1"));
    }

    @Test
    void shouldNotLetAnotherIpLockOutTheAccount() {
        failLogin("ana@teste.com", "6.6.6.6", AuthRateLimiter.MAX_LOGIN_FAILURES);

        assertDoesNotThrow(() -> limiter.checkLoginAllowed("ana@teste.com", "1.1.1.1"));
    }

    @Test
    void shouldAllowAgainAfterTheWindowExpires() {
        failLogin("ana@teste.com", "1.1.1.1", AuthRateLimiter.MAX_LOGIN_FAILURES);

        clock.advance(AuthRateLimiter.WINDOW);

        assertDoesNotThrow(() -> limiter.checkLoginAllowed("ana@teste.com", "1.1.1.1"));
    }

    @Test
    void shouldReportRemainingWaitTime() {
        failLogin("ana@teste.com", "1.1.1.1", AuthRateLimiter.MAX_LOGIN_FAILURES);
        clock.advance(Duration.ofMinutes(5));

        TooManyAttemptsException e = assertThrows(TooManyAttemptsException.class,
                () -> limiter.checkLoginAllowed("ana@teste.com", "1.1.1.1"));

        assertEquals(Duration.ofMinutes(10).toSeconds(), e.getRetryAfterSeconds());
        assertEquals("Too many attempts. Try again in 10 minute(s).", e.getMessage());
    }

    @Test
    void shouldBlockAnIpThatFailsOnManyDifferentEmails() {
        for (int i = 0; i < AuthRateLimiter.MAX_LOGIN_FAILURES_PER_IP; i++) {
            limiter.recordLoginFailure("user" + i + "@teste.com", "1.1.1.1");
        }

        assertThrows(TooManyAttemptsException.class, () -> limiter.checkLoginAllowed("brand-new@teste.com", "1.1.1.1"));
        assertDoesNotThrow(() -> limiter.checkLoginAllowed("brand-new@teste.com", "2.2.2.2"));
    }

    @Test
    void shouldClearEmailAndIpCounterOnSuccess() {
        failLogin("ana@teste.com", "1.1.1.1", AuthRateLimiter.MAX_LOGIN_FAILURES - 1);

        limiter.recordLoginSuccess("ana@teste.com", "1.1.1.1");
        failLogin("ana@teste.com", "1.1.1.1", AuthRateLimiter.MAX_LOGIN_FAILURES - 1);

        assertDoesNotThrow(() -> limiter.checkLoginAllowed("ana@teste.com", "1.1.1.1"));
    }

    @Test
    void shouldNotClearIpCounterOnSuccess() {
        for (int i = 0; i < AuthRateLimiter.MAX_LOGIN_FAILURES_PER_IP - 1; i++) {
            limiter.recordLoginFailure("user" + i + "@teste.com", "1.1.1.1");
        }

        limiter.recordLoginSuccess("mine@teste.com", "1.1.1.1");
        limiter.recordLoginFailure("one-more@teste.com", "1.1.1.1");

        assertThrows(TooManyAttemptsException.class, () -> limiter.checkLoginAllowed("anyone@teste.com", "1.1.1.1"));
    }

    @Test
    void shouldLimitPasswordResetRequestsPerEmail() {
        for (int i = 0; i < AuthRateLimiter.MAX_RESET_REQUESTS_PER_EMAIL; i++) {
            limiter.checkAndRecordPasswordResetRequest("ana@teste.com", "1.1.1." + i);
        }

        assertThrows(TooManyAttemptsException.class,
                () -> limiter.checkAndRecordPasswordResetRequest("ana@teste.com", "9.9.9.9"));
    }

    private void failLogin(String email, String ip, int times) {
        for (int i = 0; i < times; i++) {
            limiter.recordLoginFailure(email, ip);
        }
    }

    static class MutableClock extends Clock {
        private Instant now;

        MutableClock(Instant start) {
            this.now = start;
        }

        void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override
        public java.time.ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(java.time.ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return now;
        }
    }
}
