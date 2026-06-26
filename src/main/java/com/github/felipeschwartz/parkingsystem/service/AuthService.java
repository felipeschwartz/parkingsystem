package com.github.felipeschwartz.parkingsystem.service;

import com.github.felipeschwartz.parkingsystem.model.entity.Owner;
import com.github.felipeschwartz.parkingsystem.repository.OwnerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final OwnerRepository ownerRepository; // Injeção via construtor
    private final PasswordEncoder passwordEncoder; // Injeção via construtor

    public AuthService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) { // CORRIGIDO: Injeção via construtor
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean authenticate(String email, String rawPassword) {
        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return passwordEncoder.matches(rawPassword, owner.getPassword());
    }
}
