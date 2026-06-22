package com.github.felipeschwartz.parkingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.Owner;
import com.github.felipeschwartz.parkingsystem.repository.OwnerRepository;

@Service
public class AuthService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean authenticate(String email, String rawPassword) {
        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return passwordEncoder.matches(rawPassword, owner.getPassword());
    }
}
