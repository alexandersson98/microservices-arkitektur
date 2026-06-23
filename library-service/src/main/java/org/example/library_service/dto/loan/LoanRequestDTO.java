package org.example.library_service.dto.loan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request object for creating a loan")
public record LoanRequestDTO(@Schema(description = "Book id from the book planed to loan", example = "2")
                             @NotNull(message = "Book id must not be null") Long bookId) {

}
