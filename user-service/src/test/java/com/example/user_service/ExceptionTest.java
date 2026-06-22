package com.example.user_service;

import com.example.user_service.exceptions.NotFoundException;
import com.example.user_service.exceptions.NotFoundWithIdException;
import com.example.user_service.exceptions.ValidationException;
import com.example.user_service.exceptions.handler.ApiErrorResponse;
import com.example.user_service.exceptions.handler.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void notFoundExceptionShouldReturn404() {
        NotFoundException ex = new NotFoundException("Not found");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/test");

        ResponseEntity<ApiErrorResponse> response = handler.handleBookAlreadyLoaned(ex, request);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
        assertEquals("Not found", response.getBody().message());
    }

    @Test
    void notFoundWithIdExceptionShouldReturn404() {
        NotFoundWithIdException ex = new NotFoundWithIdException("Member not found with id: ", 5L);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/test");

        ResponseEntity<ApiErrorResponse> response = handler.handleBookNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
    }

    @Test
    void validationExceptionShouldReturn400() {
        ValidationException ex = new ValidationException("Validation failed");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/test");

        ResponseEntity<ApiErrorResponse> response = handler.handleValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals("Validation failed", response.getBody().message());
    }

    @Test
    void badCredentialsExceptionShouldReturn401() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/v1/auth/login");

        ResponseEntity<ApiErrorResponse> response = handler.handleBadCredentials(ex, request);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode().value());
    }

    @Test
    void notFoundExceptionMessageShouldBeCorrect() {
        NotFoundException ex = new NotFoundException("Test message");
        assertEquals("Test message", ex.getMessage());
    }

    @Test
    void notFoundWithIdExceptionMessageShouldContainId() {
        NotFoundWithIdException ex = new NotFoundWithIdException("Member not found: ", 42L);
        assertTrue(ex.getMessage().contains("42"));
    }

    @Test
    void validationExceptionMessageShouldBeCorrect() {
        ValidationException ex = new ValidationException("Invalid input");
        assertEquals("Invalid input", ex.getMessage());
    }
}