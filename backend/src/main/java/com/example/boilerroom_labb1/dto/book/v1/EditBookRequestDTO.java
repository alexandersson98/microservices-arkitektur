package com.example.boilerroom_labb1.dto.book.v1;

import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Request object for editing a book")
public record EditBookRequestDTO(
        @Schema(description = "Title of the book", example = "Stranger things")
        String title,
        @Schema(description = "Author id of the book", example = "5")
        Long authorId,
        @Schema(description = "ISBN number of the book ", example = "Ev443-FRed")
        String isbn,
        @Schema(description = "Year the book was published", example = "1924")
        Integer publishedYear) {
}