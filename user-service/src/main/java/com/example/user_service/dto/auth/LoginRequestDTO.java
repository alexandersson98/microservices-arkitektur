package com.example.user_service.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @Schema(description = "Email or phone number", example = "lukas@live.se")
        @NotBlank(message = "Username is required")
        String username,

        @Schema(description = "Password", example = "password123")
        @NotBlank(message = "Password is required")
        String password) {
}
