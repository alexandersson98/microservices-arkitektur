package com.example.user_service;

import com.example.user_service.entity.member.Member;
import com.example.user_service.entity.member.Role;
import com.example.user_service.exceptions.NotFoundException;
import com.example.user_service.exceptions.NotFoundWithIdException;
import com.example.user_service.exceptions.ValidationException;
import com.example.user_service.exceptions.handler.ApiErrorResponse;
import com.example.user_service.exceptions.handler.GlobalExceptionHandler;
import com.example.user_service.security.CustomUserDetails;
import com.example.user_service.security.JwtUtil;
import com.example.user_service.security.RateLimitingFilter;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

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

    // ===== SECURITY TESTER =====

    @Test
    void rateLimitingFilterShouldAllowRequestUnderLimit() throws Exception {
        RateLimitingFilter filter = new RateLimitingFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(200, response.getStatus());
    }

    @Test
    void customUserDetailsShouldReturnCorrectValues() {
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setPassword("encodedpassword");
        member.setRole(Role.USER);

        CustomUserDetails userDetails = new CustomUserDetails(member);

        assertEquals("test@test.com", userDetails.getUsername());
        assertEquals("encodedpassword", userDetails.getPassword());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void jwtUtilShouldGenerateAndValidateToken() throws Exception {
        JwtUtil jwtUtil = new JwtUtil();

        java.lang.reflect.Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, "dGVzdHNlY3JldGtleXRlc3RzZWNyZXRrZXl0ZXN0c2VjcmV0a2V5dGVzdA==");

        java.lang.reflect.Field expirationField = JwtUtil.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtil, 86400000L);

        String token = jwtUtil.generateToken("test@test.com", "USER");

        assertTrue(jwtUtil.validateToken(token));
        assertEquals("test@test.com", jwtUtil.extractUsername(token));
        assertEquals("USER", jwtUtil.extractRole(token));
    }

    @Test
    void jwtUtilShouldReturnFalseForInvalidToken() throws Exception {
        JwtUtil jwtUtil = new JwtUtil();

        java.lang.reflect.Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, "dGVzdHNlY3JldGtleXRlc3RzZWNyZXRrZXl0ZXN0c2VjcmV0a2V5dGVzdA==");

        java.lang.reflect.Field expirationField = JwtUtil.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtil, 86400000L);

        assertFalse(jwtUtil.validateToken("invalid.token.here"));
    }

    @Test
    void rateLimitingFilterShouldReturn429WhenLimitExceeded() throws Exception {
        RateLimitingFilter filter = new RateLimitingFilter();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.1.1");

        // Kör 201 requests för att trigga rate limit (max 200)
        for (int i = 0; i < 201; i++) {
            MockHttpServletResponse response = new MockHttpServletResponse();
            MockFilterChain chain = new MockFilterChain();
            filter.doFilter(request, response, chain);
            if (response.getStatus() == 429) {
                assertEquals(429, response.getStatus());
                return;
            }
        }
        fail("Rate limit should have been exceeded");
    }

    @Test
    void jwtAuthenticationFilterShouldSkipWhenNoAuthHeader() throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        java.lang.reflect.Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, "dGVzdHNlY3JldGtleXRlc3RzZWNyZXRrZXl0ZXN0c2VjcmV0a2V5dGVzdA==");
        java.lang.reflect.Field expirationField = JwtUtil.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtil, 86400000L);

        com.example.user_service.security.JwtAuthenticationFilter filter =
                new com.example.user_service.security.JwtAuthenticationFilter(jwtUtil, username -> {
                    Member member = new Member();
                    member.setEmail(username);
                    member.setPassword("password");
                    member.setRole(Role.USER);
                    return new CustomUserDetails(member);
                });

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        // Ingen Authorization-header
        filter.doFilter(request, response, chain);
        assertEquals(200, response.getStatus());
    }

    @Test
    void jwtAuthenticationFilterShouldSkipWhenInvalidToken() throws Exception {
        JwtUtil jwtUtil = new JwtUtil();
        java.lang.reflect.Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, "dGVzdHNlY3JldGtleXRlc3RzZWNyZXRrZXl0ZXN0c2VjcmV0a2V5dGVzdA==");
        java.lang.reflect.Field expirationField = JwtUtil.class.getDeclaredField("expiration");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtil, 86400000L);

        // Generera en riktig token och gör den ogiltig genom att ändra sista tecknet
        String validToken = jwtUtil.generateToken("test@test.com", "USER");
        String tamperedToken = validToken.substring(0, validToken.length() - 1) + "X";

        com.example.user_service.security.JwtAuthenticationFilter filter =
                new com.example.user_service.security.JwtAuthenticationFilter(jwtUtil, username -> {
                    Member member = new Member();
                    member.setEmail(username);
                    member.setPassword("password");
                    member.setRole(Role.USER);
                    return new CustomUserDetails(member);
                });

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + tamperedToken);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);
        assertEquals(200, response.getStatus());
    }
}