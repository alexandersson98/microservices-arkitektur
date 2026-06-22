package org.example.library_service.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.example.library_service.dto.book.BookRequestDTO;
import org.example.library_service.dto.book.v1.BookMetaDataResponseDTO;
import org.example.library_service.dto.book.v1.BookResponseDTO;
import org.example.library_service.dto.book.v1.EditBookRequestDTO;
import org.example.library_service.openapi.BadRequestResponse;
import org.example.library_service.openapi.NotFoundResponse;
import org.example.library_service.service.BookService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService service;


    public BookController(BookService service){
        this.service = service;
    }

    @Operation(summary = "Create book",
            description = "Add a book to the database")
            @ApiResponse(responseCode = "201", description = "Created")
    @BadRequestResponse
    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@Valid @RequestBody BookRequestDTO request) {
        BookResponseDTO response = service.createBook(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get a book with id",
            description = "Returns a book with given id")

    @ApiResponse(responseCode = "200", description = "Found")
    @NotFoundResponse
    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> getBookById(@PathVariable Long id){
        BookResponseDTO response = service.getBookById(id);
        return ResponseEntity
                .ok()
                .body(response);
    }

    @Operation(summary = "Get all books",
            description = "Returns a list of all books")
            @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public ResponseEntity <Page<BookResponseDTO>> getBooks(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(service.getAll(pageable));
    }

    @Operation(summary = "Edit book",
            description = "Edit a book")
            @ApiResponse(responseCode = "200", description = "Book successfully updated")
    @NotFoundResponse
    @PatchMapping("/edit/{id}")
    public ResponseEntity<BookResponseDTO>editBook(@PathVariable Long id, @RequestBody EditBookRequestDTO editBookRequestDto){
        BookResponseDTO response = service.editBook(id, editBookRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Get book Metadata",
    description = "Fetches metadata for a book from Open Library by ISBN")
    @ApiResponse(responseCode = "200", description = "Success")
    @NotFoundResponse
    @GetMapping("/{id}/metadata")
    public ResponseEntity<BookMetaDataResponseDTO>getBookMetaData(@PathVariable Long id){
        BookMetaDataResponseDTO metaDataResponse = service.getBookMetaData(id);
        return ResponseEntity.ok()
                .body(metaDataResponse);
    }

}
