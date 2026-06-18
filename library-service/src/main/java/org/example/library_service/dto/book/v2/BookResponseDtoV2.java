package org.example.library_service.dto.book.v2;


import io.swagger.v3.oas.annotations.media.Schema;
import org.example.library_service.dto.author.AuthorResponseDTO;

import java.io.Serializable;

@Schema(description = "Version 2 response object representing a book")
public record BookResponseDtoV2(

        @Schema(description = "Unique identifier of the book", example = "1")
        Long id,

        @Schema(description = "Title of the book", example = "Harry Potter")
        String title,

        @Schema(description = "Author of the book", example = "J.K. Rowling")
        AuthorResponseDTO author,

        @Schema(description = "ISBN number of the book", example = "9780747532743")
        String isbn,

        @Schema(description = "Year the book was published", example = "1997")
        int publishedYear,

        @Schema(description = "Availability status of the book", example = "true")
        boolean available
) implements Serializable {}