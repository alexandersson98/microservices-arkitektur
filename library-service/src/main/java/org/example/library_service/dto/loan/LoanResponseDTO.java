package org.example.library_service.dto.loan;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.library_service.dto.member.MemberLoanResponseDTO;

import java.io.Serializable;
import java.time.LocalDate;
@Schema(description = "Response object representing a loan")
public record LoanResponseDTO(@Schema(description = "Unique identifier of the book", example = "2")Long id,
                              Long bookId,
                              @Schema(description = "Title of the book", example = "Hunger Games")
                              String bookTitle,
                              LocalDate loanDate,
                              Long memberId,
                              String memberName) implements Serializable {}
