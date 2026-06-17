package com.example.boilerroom_labb1.controller;

import com.example.boilerroom_labb1.dto.auth.LoginRequestDTO;
import com.example.boilerroom_labb1.dto.auth.LoginResponseDTO;
import com.example.boilerroom_labb1.openapi.BadRequestResponse;
import com.example.boilerroom_labb1.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Login member",
    description = "Login a member to the website")
    @ApiResponse(responseCode = "200", description = "Login success")
    @BadRequestResponse
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO>login (@RequestBody LoginRequestDTO request){
       LoginResponseDTO responseDTO = authService.login(request);
       return ResponseEntity.status(HttpStatus.OK)
               .body(responseDTO);
    }
}
