package org.example.library_service.openapi;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.library_service.exceptions.handler.ApiErrorResponse;


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
