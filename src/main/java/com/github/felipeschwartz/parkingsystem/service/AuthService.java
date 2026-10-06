package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.config.CustomUserDetails;
import com.github.felipeschwartz.parkingsystem.config.JwtService;
import com.github.felipeschwartz.parkingsystem.model.dto.LoginRequestDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.LoginResponseDTO;
import com.github.felipeschwartz.parkingsystem.model.dto.UserSummaryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthRateLimiter rateLimiter;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthRateLimiter rateLimiter, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.rateLimiter = rateLimiter;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO request, String clientIp) {
        logger.info("Authenticating user: {}", request.email());
        rateLimiter.checkLoginAllowed(request.email(), clientIp);
        Authentication auth;
        try {
            auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (AuthenticationException e) {
            rateLimiter.recordLoginFailure(request.email(), clientIp);
            throw e;
        }
        rateLimiter.recordLoginSuccess(request.email(), clientIp);

        CustomUserDetails principal = (CustomUserDetails) auth.getPrincipal();
        String token = jwtService.generateToken(principal.getEmail());

        UserSummaryDTO userSummary = new UserSummaryDTO(
                principal.getId(),
                principal.getName(),
                principal.getEmail(),
                principal.getRoles()
        );

        return new LoginResponseDTO(token, userSummary);
    }
}
