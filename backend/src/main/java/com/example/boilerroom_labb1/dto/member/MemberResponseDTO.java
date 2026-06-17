package com.example.boilerroom_labb1.dto.member;


import io.swagger.v3.oas.annotations.media.Schema;

public record MemberResponseDTO(
        @Schema(description = "Full name of the member", example = "Lukas Alexandersson")
        String name,

        @Schema(description = "Phone number", example = "0723334455")
        String phone,

        @Schema(description = "Personal ID number", example = "19980101-1234")
        String personId,

        @Schema(description = "Email address", example = "lukas@live.se")
        String email) {
}
