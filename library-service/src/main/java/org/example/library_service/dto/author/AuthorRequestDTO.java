package org.example.library_service.dto.author;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request object for creating an author")
public record AuthorRequestDTO(
        @Schema(description = "Name of the author", example = "Jk Rowling")
        @NotBlank(message = "Name must not be blank")
        String name

) {
}
