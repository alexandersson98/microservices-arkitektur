package org.example.library_service.controller;



import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.library_service.dto.author.AuthorRequestDTO;
import org.example.library_service.dto.author.AuthorResponseDTO;
import org.example.library_service.dto.book.v1.BookResponseDTO;
import org.example.library_service.openapi.BadRequestResponse;
import org.example.library_service.openapi.NotFoundResponse;
import org.example.library_service.service.AuthorService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/v1/author")
public class AuthorController {
    private final AuthorService service;

    public AuthorController (AuthorService service){
        this.service = service;
    }

    @Operation(summary = "Create author",
            description = "Add an author to the database"
    )
    @ApiResponse(responseCode = "201", description = "Created")
    @BadRequestResponse
    @PostMapping
    public ResponseEntity<AuthorResponseDTO>create(@Valid @RequestBody AuthorRequestDTO request){
        AuthorResponseDTO response = service.createEntity(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get author by id",
    description = "Returns an author with entered id")
    @ApiResponse(responseCode = "200", description = "Found")
    @NotFoundResponse
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO>getAuthorById(@PathVariable Long id){
        AuthorResponseDTO response = service.getAuthorById(id);
        return ResponseEntity.ok()
                .body(response);
    }
    @Operation(summary = "Get books by author id",
    description = "Returns a list with all books by an author")
    @ApiResponse(responseCode = "200", description = "success")
    @NotFoundResponse
    @GetMapping("/{authorId}/books")
    public ResponseEntity<Page<BookResponseDTO>> getAllBooksByAuthorId(@PathVariable Long authorId, @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(service.getBooksByAuthor(authorId, pageable));
    }
}
