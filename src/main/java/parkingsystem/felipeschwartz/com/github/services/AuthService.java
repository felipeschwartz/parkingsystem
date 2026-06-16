package parkingsystem.felipeschwartz.com.github.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerRepository;

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
