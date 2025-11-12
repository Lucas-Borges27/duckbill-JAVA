package com.db.skillup.web.dto;

public record AuthResponse(String token, String type, long expiresInMs) {}
