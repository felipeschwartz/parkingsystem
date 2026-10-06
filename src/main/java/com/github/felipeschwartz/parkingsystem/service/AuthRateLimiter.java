package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.service.exceptions.TooManyAttemptsException;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.util.Locale;

@Component
public class AuthRateLimiter {

    static final int MAX_LOGIN_FAILURES = 5;
    static final int MAX_LOGIN_FAILURES_PER_IP = 20;
    static final int MAX_RESET_REQUESTS_PER_EMAIL = 3;
    static final int MAX_RESET_REQUESTS_PER_IP = 10;
    static final Duration WINDOW = Duration.ofMinutes(15);

    private final FixedWindowRateLimiter loginFailures;
    private final FixedWindowRateLimiter loginFailuresByIp;
    private final FixedWindowRateLimiter resetRequestsByEmail;
    private final FixedWindowRateLimiter resetRequestsByIp;

    public AuthRateLimiter() {
        this(Clock.systemUTC());
    }

    AuthRateLimiter(Clock clock) {
        this.loginFailures = new FixedWindowRateLimiter(MAX_LOGIN_FAILURES, WINDOW, clock);
        this.loginFailuresByIp = new FixedWindowRateLimiter(MAX_LOGIN_FAILURES_PER_IP, WINDOW, clock);
        this.resetRequestsByEmail = new FixedWindowRateLimiter(MAX_RESET_REQUESTS_PER_EMAIL, WINDOW, clock);
        this.resetRequestsByIp = new FixedWindowRateLimiter(MAX_RESET_REQUESTS_PER_IP, WINDOW, clock);
    }

    // Dois limites: por e-mail+IP (protege uma conta sem permitir que terceiros a bloqueiem de outro IP)
    // e por IP (impede que um único IP teste muitos e-mails).
    public void checkLoginAllowed(String email, String clientIp) {
        throwIfBlocked(Math.max(
                loginFailures.secondsUntilAllowed(loginKey(email, clientIp)),
                loginFailuresByIp.secondsUntilAllowed(ipKey(clientIp))));
    }

    public void recordLoginFailure(String email, String clientIp) {
        loginFailures.recordAttempt(loginKey(email, clientIp));
        loginFailuresByIp.recordAttempt(ipKey(clientIp));
    }

    // Só zera o contador e-mail+IP: se zerasse o do IP, quem tem uma conta válida poderia reiniciá-lo entre as tentativas.
    public void recordLoginSuccess(String email, String clientIp) {
        loginFailures.reset(loginKey(email, clientIp));
    }

    // Limita antes de consultar o usuário, então a resposta não revela se o e-mail existe.
    public void checkAndRecordPasswordResetRequest(String email, String clientIp) {
        String emailKey = normalize(email);
        String ipKey = ipKey(clientIp);
        throwIfBlocked(Math.max(
                resetRequestsByEmail.secondsUntilAllowed(emailKey),
                resetRequestsByIp.secondsUntilAllowed(ipKey)));
        resetRequestsByEmail.recordAttempt(emailKey);
        resetRequestsByIp.recordAttempt(ipKey);
    }

    private static void throwIfBlocked(long secondsUntilAllowed) {
        if (secondsUntilAllowed > 0) {
            throw new TooManyAttemptsException(secondsUntilAllowed);
        }
    }

    private static String loginKey(String email, String clientIp) {
        return normalize(email) + "|" + ipKey(clientIp);
    }

    private static String ipKey(String clientIp) {
        return String.valueOf(clientIp);
    }

    private static String normalize(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }
}
