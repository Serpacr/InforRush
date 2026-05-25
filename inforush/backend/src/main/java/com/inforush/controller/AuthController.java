package com.inforush.controller;

import com.inforush.dto.AuthDTO;
import com.inforush.model.User;
import com.inforush.repository.UserRepository;
import com.inforush.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    @Operation(summary = "Cadastrar novo usuário")
    public ResponseEntity<?> register(@Valid @RequestBody AuthDTO.RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail()))
            return ResponseEntity.status(409).body(Map.of("error", "E-mail já cadastrado"));

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        userRepository.save(user);

        String token = jwtUtil.generateToken(user);
        return ResponseEntity.status(201).body(new AuthDTO.AuthResponse(AuthDTO.UserDTO.from(user), token));
    }

    @PostMapping("/login")
    @Operation(summary = "Fazer login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTO.LoginRequest req) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciais inválidas"));
        }
        User user = userRepository.findByEmail(req.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken(user);
        return ResponseEntity.ok(new AuthDTO.AuthResponse(AuthDTO.UserDTO.from(user), token));
    }

    @GetMapping("/me")
    @Operation(summary = "Dados do usuário autenticado")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(AuthDTO.UserDTO.from(user));
    }
}
