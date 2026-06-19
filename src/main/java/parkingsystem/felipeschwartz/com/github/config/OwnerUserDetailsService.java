package parkingsystem.felipeschwartz.com.github.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import parkingsystem.felipeschwartz.com.github.model.entities.Owner;
import parkingsystem.felipeschwartz.com.github.repositories.OwnerRepository;

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