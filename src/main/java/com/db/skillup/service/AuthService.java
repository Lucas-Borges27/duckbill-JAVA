package com.db.skillup.service;

import com.db.skillup.security.JwtTokenProvider;
import com.db.skillup.web.dto.AuthRequest;
import com.db.skillup.web.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Value("${jwt.expiration-ms:3600000}")
    private long expirationMs;

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha()));
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        String token = tokenProvider.generateToken(principal);
        return new AuthResponse(token, "Bearer", expirationMs);
    }
}
