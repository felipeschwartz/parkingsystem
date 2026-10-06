package com.github.felipeschwartz.parkingsystem.config;

import io.jsonwebtoken.security.WeakKeyException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private static final String SECRET = "test-only-secret-with-at-least-32-bytes!!";

    private final JwtService service = new JwtService(SECRET, 60_000);

    @Test
    void shouldFailToStartWithASecretShorterThan32Bytes() {
        assertThrows(WeakKeyException.class, () -> new JwtService("too-short", 60_000));
    }

    @Test
    void shouldIssueATokenThatCarriesTheEmail() {
        String token = service.generateToken("ana@teste.com");

        assertTrue(service.isTokenValid(token));
        assertEquals("ana@teste.com", service.extractEmail(token));
    }

    @Test
    void shouldRejectATokenSignedWithAnotherSecret() {
        JwtService other = new JwtService("another-secret-that-is-also-32-bytes-long", 60_000);

        assertFalse(service.isTokenValid(other.generateToken("ana@teste.com")));
    }

    @Test
    void shouldRejectAnExpiredToken() {
        JwtService shortLived = new JwtService(SECRET, -1_000);

        assertFalse(service.isTokenValid(shortLived.generateToken("ana@teste.com")));
    }

    @Test
    void shouldRejectATamperedToken() {
        String token = service.generateToken("ana@teste.com");
        String tampered = token.substring(0, token.length() - 2) + (token.endsWith("A") ? "BB" : "AA");

        assertFalse(service.isTokenValid(tampered));
    }

    @Test
    void shouldRejectGarbage() {
        assertFalse(service.isTokenValid("not-a-jwt"));
    }
}
