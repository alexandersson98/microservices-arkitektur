package com.example.user_service.dto.member;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MemberRequestDTO(
        @Schema(description = "Full name of the member", example = "Lukas Alexandersson")
        @NotBlank(message = "Name is required")
        String name,

        @Schema(description = "Phone number", example = "0723334455")
        @NotBlank(message = "Phone is required")
        String phone,

        @Schema(description = "Personal ID number", example = "19980101-1234")
        @NotBlank(message = "Person ID is required")
        String personId,

        @Schema(description = "Email address", example = "lukas@live.se")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "Password", example = "password123")
        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password
){}
