package com.example.hms.controller;

import com.example.hms.config.JwtService;
import com.example.hms.dto.AuthRequest;
import com.example.hms.entity.User;
import com.example.hms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("User already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return ResponseEntity.ok(token);
    }
}