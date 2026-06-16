package parkingsystem.felipeschwartz.com.github.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parkingsystem.felipeschwartz.com.github.data.dto.LoginRequestDTO;
import parkingsystem.felipeschwartz.com.github.services.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login() { //@RequestBody @Valid LoginRequestDTO dto
        // boolean authenticated = authService.authenticate(dto.getUsername(), dto.getPassword());
//        if (authenticated) {
//            return ResponseEntity.ok().body("Login successful");
//        } else  {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(Map.of("username", auth.getName(), "roles", auth.getAuthorities()));
    }
}
