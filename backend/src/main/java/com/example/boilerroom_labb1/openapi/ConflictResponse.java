package com.example.boilerroom_labb1.openapi;


import com.example.boilerroom_labb1.exceptions.handler.ApiErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@ApiResponse(
        responseCode = "409",
        description = "Conflict: the book is already loaned and cannot be loaned again.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiErrorResponse.class)
        )
)
public @interface ConflictResponse {
}
