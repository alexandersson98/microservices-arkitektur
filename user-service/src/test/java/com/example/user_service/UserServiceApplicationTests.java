package com.example.user_service;

import com.example.user_service.entity.member.Member;
import com.example.user_service.entity.member.Role;
import com.example.user_service.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserServiceApplicationTests {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("userdb_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        // Vault-bypass för tester
        registry.add("jwt.secret", () -> "dGVzdHNlY3JldGtleXRlc3RzZWNyZXRrZXl0ZXN0c2VjcmV0a2V5dGVzdA==");
        registry.add("jwt.expiration", () -> "86400000");
        registry.add("spring.cloud.vault.enabled", () -> "false");
        registry.add("spring.config.import", () -> "");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        Member member = new Member();
        member.setEmail("test@test.com");
        member.setPassword(passwordEncoder.encode("password123"));
        member.setName("Test User");
        member.setRole(Role.USER);
        memberRepository.save(member);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void loginShouldReturnJwtToken() throws Exception {
        String loginRequest = """
                {
                    "username": "test@test.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void loginWithWrongPasswordShouldReturn401() throws Exception {
        String loginRequest = """
                {
                    "username": "test@test.com",
                    "password": "wrongpassword"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized());
    }
}