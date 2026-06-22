package com.github.felipeschwartz.parkingsystem.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.github.felipeschwartz.parkingsystem.model.entity.Owner;
import com.github.felipeschwartz.parkingsystem.repository.OwnerRepository;

@Service
public class OwnerUserDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;

    public OwnerUserDetailsService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Owner owner = ownerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return User.builder()
                .username(owner.getEmail())
                .password(owner.getPassword())
                .roles("USER")
                .build();
    }
}