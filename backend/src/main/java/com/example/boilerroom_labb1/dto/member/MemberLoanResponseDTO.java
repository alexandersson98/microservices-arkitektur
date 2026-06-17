package com.example.boilerroom_labb1.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

public record MemberLoanResponseDTO(
        @Schema(description = "Full name of the member", example = "Lukas Alexandersson")
        String name,

        @Schema(description = "Unique identifier of the member", example = "1")
        Long id)implements Serializable {}