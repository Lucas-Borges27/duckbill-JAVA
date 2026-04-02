package com.db.duckbill.web.controller.api;

import com.db.duckbill.security.JwtService;
import com.db.duckbill.service.UsuarioService;
import com.db.duckbill.web.dto.AuthLoginRequest;
import com.db.duckbill.web.dto.AuthRegisterRequest;
import com.db.duckbill.web.dto.AuthResponseDTO;
import com.db.duckbill.web.dto.UsuarioDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody AuthLoginRequest request) {
        String email = request.email().trim().toLowerCase();
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.senha())
            );
        } catch (AuthenticationException ex) {
            throw new IllegalArgumentException("Email ou senha inválidos.");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        var usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        String token = jwtService.generateToken(userDetails, usuario.getId(), usuario.getRole());

        return ResponseEntity.ok(new AuthResponseDTO(
            token,
            "Bearer",
            jwtService.getExpirationSeconds(),
            UsuarioDTO.fromEntity(usuario)
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody AuthRegisterRequest request) {
        var usuario = usuarioService.registrar(request);
        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername(usuario.getEmail())
            .password(usuario.getSenha())
            .authorities(usuario.getRole())
            .build();
        String token = jwtService.generateToken(userDetails, usuario.getId(), usuario.getRole());

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponseDTO(
            token,
            "Bearer",
            jwtService.getExpirationSeconds(),
            UsuarioDTO.fromEntity(usuario)
        ));
    }
}
