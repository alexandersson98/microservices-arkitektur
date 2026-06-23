package org.example.library_service.dto.loan;

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
        @Schema(description = "Id of the member", example = "4")
        Long memberId,
        @Schema(description = "name of the member", example = "Andreas Larsson")
        String memberName
) implements Serializable {}
