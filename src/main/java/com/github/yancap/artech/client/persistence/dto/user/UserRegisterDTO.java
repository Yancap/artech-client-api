package com.github.yancap.artech.client.persistence.dto.user;

public record UserRegisterDTO(
        String name,
        String email,
        String password,
        String imageBlob
) {
}
