package com.example.boilerroom_labb1.dto.auth;

import com.example.boilerroom_labb1.entity.member.Role;
import io.swagger.v3.oas.annotations.media.Schema;


public record LoginResponseDTO(
        @Schema(description = "JWT token used for authentication", example = "eyJhbGciOiJIUzI1NiJ9...")
        String token,

        @Schema(description = "Role of the authenticated user", example = "USER")
        Role role) {
}