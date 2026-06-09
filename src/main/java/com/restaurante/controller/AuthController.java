package com.restaurante.controller;

import com.restaurante.dto.LoginRequest;
import com.restaurante.dto.LoginResponse;
import com.restaurante.model.Usuario;
import com.restaurante.repository.UsuarioRepository;
import com.restaurante.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepo;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        Usuario u = usuarioRepo.findByUsername(req.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(u.getUsername(), u.getRol());
        return ResponseEntity.ok(new LoginResponse(token, u.getRol()));
    }
}
