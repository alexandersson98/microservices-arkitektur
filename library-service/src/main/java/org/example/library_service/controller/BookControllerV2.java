package org.example.library_service.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.example.library_service.dto.book.BookRequestDTO;
import org.example.library_service.dto.book.v2.BookResponseDtoV2;
import org.example.library_service.dto.book.v2.BookWrapperDtoV2;
import org.example.library_service.dto.book.v2.BookWrapperGetByIdDtoV2;
import org.example.library_service.openapi.BadRequestResponse;
import org.example.library_service.openapi.NotFoundResponse;
import org.example.library_service.service.BookService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v2/books")
public class BookControllerV2 {
    private final BookService service;



    public BookControllerV2(BookService service){
        this.service = service;
    }

    @Operation(summary = "Create book",
    description = "Add a book to the database")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Created"),
    })
    @BadRequestResponse
    @PostMapping
    public ResponseEntity<BookResponseDtoV2> create(@Valid @RequestBody BookRequestDTO request) {
       BookResponseDtoV2 response = service.createBookV2(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get a book with id",
    description = "Using version2 returns a book with given id")

            @ApiResponse(responseCode = "200", description = "Found")
    @NotFoundResponse
    @GetMapping("/{id}")
    public ResponseEntity<BookWrapperGetByIdDtoV2>getBookById(@PathVariable Long id){
        BookWrapperGetByIdDtoV2 response = service.getBookByIdV2(id);
        return ResponseEntity
                .ok()
                .body(response);
    }
    @Operation(summary = "Get all books",
    description = "Using version2 returns a wrapped list of all books")
            @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public ResponseEntity <BookWrapperDtoV2>getAllV2(@ParameterObject Pageable pageable) {
        BookWrapperDtoV2 response = service.getAllV2(pageable);
        return ResponseEntity
                .ok()
                .body(response);
    }
}
