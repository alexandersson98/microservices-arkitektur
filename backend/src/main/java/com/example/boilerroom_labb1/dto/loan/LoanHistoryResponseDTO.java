package com.example.boilerroom_labb1.dto.loan;

import com.example.boilerroom_labb1.dto.member.MemberLoanResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDate;

public record LoanHistoryResponseDTO(

        @Schema(description = "Unique identifier of the loan", example = "1")
        Long id,

        @Schema(description = "Title of the returned book", example = "Harry Potter")
        String bookTitle,

        @Schema(description = "Date the book was loaned", example = "2026-04-01")
        LocalDate loanDate,

        @Schema(description = "Date the book was returned", example = "2026-04-16")
        LocalDate returnDate,

        @Schema(description = "Status message", example = "Book has been returned")
        String message,

        MemberLoanResponseDTO member
) implements Serializable {}
