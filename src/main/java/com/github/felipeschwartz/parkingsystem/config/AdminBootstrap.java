package com.github.felipeschwartz.parkingsystem.config;

import com.github.felipeschwartz.parkingsystem.model.entity.UserIndividual;
import com.github.felipeschwartz.parkingsystem.model.enums.UserProfile;
import com.github.felipeschwartz.parkingsystem.model.enums.UserType;
import com.github.felipeschwartz.parkingsystem.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Order(2)
public class AdminBootstrap implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AdminBootstrap.class);

    static final String ADMIN_ROLE = "ROLE_ADMIN";
    static final int MIN_PASSWORD_LENGTH = 12;
    // user_individuals.cpf é NOT NULL; o ADMIN inicial não tem CPF, então ocupa este valor até ser atualizado.
    static final String PLACEHOLDER_CPF = "00000000000";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String email;
    private final String password;

    public AdminBootstrap(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          @Value("${bootstrap.admin.email:}") String email,
                          @Value("${bootstrap.admin.password:}") String password) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.email = email.trim();
        this.password = password;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (email.isEmpty() && password.isEmpty()) {
            return;
        }
        validate();

        if (userRepository.existsByRole(ADMIN_ROLE)) {
            logger.info("An ADMIN user already exists; skipping the initial admin bootstrap.");
            return;
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("ADMIN_EMAIL is already used by a user that is not an ADMIN; "
                    + "refusing to promote it. Use another email.");
        }

        UserIndividual admin = new UserIndividual();
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setCpf(PLACEHOLDER_CPF);
        admin.setFirstName("Administrator");
        admin.setUserType(UserType.INDIVIDUAL);
        admin.setUserProfile(UserProfile.ADMIN);
        admin.setRoles(UserProfile.ADMIN.roles());
        userRepository.save(admin);

        logger.info("Created the initial ADMIN user {}.", email);
    }

    private void validate() {
        if (email.isEmpty() || password.isEmpty()) {
            throw new IllegalStateException("ADMIN_EMAIL and ADMIN_PASSWORD must be set together.");
        }
        if (!email.contains("@")) {
            throw new IllegalStateException("ADMIN_EMAIL is not a valid email address.");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalStateException("ADMIN_PASSWORD must have at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
    }
}
