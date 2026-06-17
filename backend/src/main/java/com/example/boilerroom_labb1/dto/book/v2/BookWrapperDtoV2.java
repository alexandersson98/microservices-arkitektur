package com.example.boilerroom_labb1.dto.book.v2;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.io.Serializable;

@Schema(description = "Wrapper object for version 2 book responses")
public record BookWrapperDtoV2(

        @Schema(
                description = "List of books in version 2 format",
                implementation = BookResponseDtoV2.class
        )
        Page<BookResponseDtoV2> data,

        @Schema(
                description = "API version",
                example = "v2"
        )
        String version

)implements Serializable {}