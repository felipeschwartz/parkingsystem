package com.github.felipeschwartz.parkingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collections;

@Configuration
@Profile("dev")
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfigDev {

    private final UserDetailsService userDetailsService;
    private final JwtFilter jwtFilter;

    public SecurityConfigDev(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/error", "/api/test/v1").permitAll() // TestLogController pode ser acessado por todos
                        // Proteção de endpoints por URL
                        .requestMatchers(HttpMethod.POST, "/api/user/v1").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/user/v1/{id}").hasAnyRole("ADMIN", "USER", "PARKING", "PARKING_MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/user/v1/id/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/user/v1").hasAnyRole("ADMIN", "PARKING_MANAGER")
                        // consulta por id/cpf/cnpj: o próprio usuário também pode, quem decide é o @PreAuthorize do UserService
                        .requestMatchers(HttpMethod.GET, "/api/user/v1/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/vehicle/v1/**").hasAnyRole("ADMIN", "PARKING_MANAGER", "PARKING", "USER")
                        .requestMatchers(HttpMethod.POST, "/api/vehicle/v1").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.PUT, "/api/vehicle/v1/{id}").hasAnyRole("ADMIN", "USER")
                        .requestMatchers(HttpMethod.DELETE, "/api/vehicle/v1/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/parking_lot/v1/**", "/api/parking_space/v1/**", "/api/hourly_rate/v1/**", "/api/plan/v1/**", "/api/plan_rate/v1/**").hasAnyRole("ADMIN", "PARKING_MANAGER")
                        .requestMatchers("/api/contracts/v1/**").hasAnyRole("ADMIN", "PARKING_MANAGER", "USER")
                        .requestMatchers("/api/reservation/v1/**").hasAnyRole("ADMIN", "PARKING_MANAGER", "USER")
                        .requestMatchers("/api/payment/v1/**").hasAnyRole("ADMIN", "PARKING_MANAGER")
                        .requestMatchers(HttpMethod.POST, "/api/parking_sessions/v1/open", "/api/parking_sessions/v1/{id}/close").hasAnyRole("ADMIN", "PARKING", "PARKING_MANAGER")
                        .requestMatchers(HttpMethod.GET, "/api/parking_sessions/v1/**").hasAnyRole("ADMIN", "PARKING", "PARKING_MANAGER", "USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Collections.singletonList(authenticationProvider()));
    }
}