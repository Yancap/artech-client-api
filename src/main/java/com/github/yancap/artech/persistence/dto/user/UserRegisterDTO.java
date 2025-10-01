package com.github.yancap.artech.persistence.dto.user;

public record UserRegisterDTO(
        String name,
        String email,
        String password,
        String imageBlob
) {
}
