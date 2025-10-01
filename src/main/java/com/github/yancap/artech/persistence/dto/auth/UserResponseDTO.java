package com.github.yancap.artech.persistence.dto.auth;

import org.eclipse.microprofile.jwt.JsonWebToken;

public record UserResponseDTO(String name, String email,  String urlAvatar) {
    public UserResponseDTO(JsonWebToken jwt){
        this(
            jwt.<String>getClaim("name"),
            jwt.<String>getClaim("email"),
            jwt.<String>getClaim("urlAvatar")
        );
    }
}
