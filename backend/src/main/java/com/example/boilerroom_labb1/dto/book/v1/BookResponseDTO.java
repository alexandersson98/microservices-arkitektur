package com.example.boilerroom_labb1.dto.book.v1;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Response object representing a book")
public record BookResponseDTO(

        @Schema(description = "Unique identifier of the book", example = "1")
        Long id,

        @Schema(description = "Title of the book", example = "Harry Potter")
        String title,

        @Schema(description = "Author of the book", example = "J.K. Rowling")
        String authorName,

        @Schema(description = "ISBN number of the book", example = "9780747532743")
        String isbn,

        @Schema(description = "Year the book was published", example = "1997")
        int publishedYear,

        @Schema(description = "Version number for optimistic locking", example = "2")
        Long version

) implements Serializable {}