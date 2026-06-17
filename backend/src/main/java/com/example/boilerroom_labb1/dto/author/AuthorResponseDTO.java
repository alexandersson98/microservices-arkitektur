package com.example.boilerroom_labb1.dto.author;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Response object representing an author")
public record AuthorResponseDTO(

        @Schema(description = "Unique identifier of the author", example = "1")
        Long id,

        @Schema(description = "name of the author", example = "Jk Rowling")
        String name
) implements Serializable {}
