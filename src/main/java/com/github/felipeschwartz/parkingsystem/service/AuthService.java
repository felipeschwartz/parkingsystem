package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.model.entity.User;
import com.github.felipeschwartz.parkingsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository; // Injeção via construtor
    private final PasswordEncoder passwordEncoder; // Injeção via construtor

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) { // CORRIGIDO: Injeção via construtor
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String email, String rawPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
