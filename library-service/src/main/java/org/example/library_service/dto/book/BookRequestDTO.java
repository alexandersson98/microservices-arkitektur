package org.example.library_service.dto.book;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request object for creating a book")
public record BookRequestDTO(
        @Schema(description = "Title of the book", example = "Stranger things")
        @NotBlank(message = "Title must not be blank")
        String title,
        @Schema(description = "Author id of the book", example = "5")
        @NotNull(message = "AuthorId must not be null")
        Long authorId,
        @Schema(description = "ISBN number of the book ", example = "Ev443-FRed")
        @NotBlank(message = "ISBN must not be blank")
        String isbn,
        @Schema(description = "Year the book was published", example = "1924")
        int publishedYear) {
}
