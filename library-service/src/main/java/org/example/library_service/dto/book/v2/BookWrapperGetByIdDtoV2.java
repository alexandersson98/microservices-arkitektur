package org.example.library_service.dto.book.v2;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Wrapper object for version 2 book responses")

public record BookWrapperGetByIdDtoV2(
        @Schema(
                description = "Return a book in version 2 format",
                implementation = BookResponseDtoV2.class
        )
        BookResponseDtoV2 data,

        @Schema(
                description = "API version",
                example = "v2"
        )
        String version
) implements Serializable {}