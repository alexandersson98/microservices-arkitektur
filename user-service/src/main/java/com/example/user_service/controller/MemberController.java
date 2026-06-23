package com.example.user_service.controller;


import com.example.user_service.dto.member.MemberLoanResponseDTO;
import com.example.user_service.dto.member.MemberRequestDTO;
import com.example.user_service.dto.member.MemberResponseDTO;
import com.example.user_service.openapi.BadRequestResponse;
import com.example.user_service.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController( MemberService memberService){
        this.memberService = memberService;
    }


    @Operation(summary = "Create member",
            description = "Register new member to database"
    )
    @ApiResponse(responseCode = "201", description = "Created")
    @BadRequestResponse
    @PostMapping
    public ResponseEntity<MemberResponseDTO>createMember(@Valid @RequestBody MemberRequestDTO memberRequestDto){
        MemberResponseDTO response =  memberService.createMember(memberRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @Operation(summary = "Create admin",
            description = "Register new admin to database"
    )
    @ApiResponse(responseCode = "201", description = "Created")
    @BadRequestResponse
    @PostMapping("/admin")
    public ResponseEntity<MemberResponseDTO>createAdmin(@Valid @RequestBody MemberRequestDTO memberRequestDto){
        MemberResponseDTO response =  memberService.createAdmin(memberRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @Operation(summary = "Create librarian",
            description = "Register new librarian to database"
    )
    @ApiResponse(responseCode = "201", description = "Created")
    @BadRequestResponse

    @PostMapping("/librarian")
    public ResponseEntity<MemberResponseDTO>createLibrarian(@Valid @RequestBody MemberRequestDTO memberRequestDto){
        MemberResponseDTO response =  memberService.createLibrarian(memberRequestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Get member for loan", description = "Returns member info used by library-service")
    @GetMapping("{id}")
    public ResponseEntity<MemberLoanResponseDTO>getMember(@PathVariable Long id){
        MemberLoanResponseDTO response = memberService.getMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
