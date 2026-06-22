package org.example.library_service.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.example.library_service.dto.loan.LoanHistoryResponseDTO;
import org.example.library_service.dto.loan.LoanRequestDTO;
import org.example.library_service.dto.loan.LoanResponseDTO;
import org.example.library_service.openapi.BadRequestResponse;
import org.example.library_service.openapi.ConflictResponse;
import org.example.library_service.service.LoanService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping ("/api/v1/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }


    @Operation(summary = "Create loan",
    description = "Create loan and add to database")
            @ApiResponse(responseCode = "201", description = "Created")
            @BadRequestResponse
            @ConflictResponse
    @PostMapping
    public ResponseEntity<LoanResponseDTO>createLoan(@RequestBody LoanRequestDTO request){
        return ResponseEntity.status(HttpStatus.CREATED).body(loanService.createLoan(request));
    }


    @Operation(summary = "Get all active loans",
    description = "Returns a list of all active loans")
            @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public ResponseEntity<Page<LoanResponseDTO>>getActiveLoans(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(loanService.getAllActiveLoans(pageable));
    }
    @Operation(summary = "Return book",
    description = "Returns a loaned book by setting the return date to now")
    @ApiResponse(responseCode = "200", description = "The book has been returned and the loan is no longer active")
    @PatchMapping("/{id}")
    public ResponseEntity<LoanHistoryResponseDTO>returnBook(@PathVariable Long id){
        LoanHistoryResponseDTO response = loanService.returnBook(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "Get loan history",
    description = "Returns a list of loan history")
    @GetMapping("/history")
    @ApiResponse(responseCode = "200", description = "success")
    public ResponseEntity<Page<LoanHistoryResponseDTO>>getLoanHistoryList(@ParameterObject Pageable pageable){
        return ResponseEntity.ok(loanService.getLoanHistoryList(pageable));
    }



}
