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
        member.setPhone("0723334455");
        member.setPersonId("19980101-1234");
        member.setRole(Role.USER);
        memberRepository.save(member);
    }

    // ===== AUTH TESTER =====

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

    @Test
    void loginWithNonExistentUserShouldReturn401() throws Exception {
        String loginRequest = """
                {
                    "username": "nonexistent@test.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isUnauthorized());
    }

    // ===== MEMBER TESTER =====

    @Test
    void createMemberShouldReturn201() throws Exception {
        String memberRequest = """
                {
                    "name": "New User",
                    "phone": "0711112222",
                    "personId": "20000101-1234",
                    "email": "newuser@test.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("newuser@test.com"))
                .andExpect(jsonPath("$.name").value("New User"));
    }

    @Test
    void createMemberWithInvalidEmailShouldReturn400() throws Exception {
        String memberRequest = """
                {
                    "name": "Bad User",
                    "phone": "0711112222",
                    "personId": "20000101-1234",
                    "email": "not-an-email",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMemberWithShortPasswordShouldReturn400() throws Exception {
        String memberRequest = """
                {
                    "name": "Bad User",
                    "phone": "0711112222",
                    "personId": "20000101-1234",
                    "email": "bad@test.com",
                    "password": "123"
                }
                """;

        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberRequest))
                .andExpect(status().isBadRequest());
    }

    // ===== ROLLBASERAD ÅTKOMSTKONTROLL =====

    @Test
    void createAdminWithoutTokenShouldReturn401() throws Exception {
        String memberRequest = """
                {
                    "name": "Admin User",
                    "phone": "0711112222",
                    "personId": "19900101-1234",
                    "email": "admin@test.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/member/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createLibrarianWithoutTokenShouldReturn401() throws Exception {
        String memberRequest = """
                {
                    "name": "Librarian User",
                    "phone": "0711112222",
                    "personId": "19900101-5678",
                    "email": "librarian@test.com",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/api/v1/member/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberRequest))
                .andExpect(status().isUnauthorized());
    }

    // ===== MEMBER SERVICE TESTER =====

    @Test
    void createAdminWithValidTokenShouldReturn201() throws Exception {
        // Först logga in som admin för att få token
        Member admin = new Member();
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setName("Admin User");
        admin.setPhone("0711111111");
        admin.setPersonId("19900101-1234");
        admin.setRole(Role.ADMIN);
        memberRepository.save(admin);

        String loginRequest = """
            {
                "username": "admin@test.com",
                "password": "password123"
            }
            """;

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(response)
                .get("token")
                .asText();

        String memberRequest = """
            {
                "name": "New Admin",
                "phone": "0722222222",
                "personId": "19950101-5678",
                "email": "newadmin@test.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/v1/member/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberRequest)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("newadmin@test.com"));
    }

    @Test
    void createLibrarianWithValidTokenShouldReturn201() throws Exception {
        Member admin = new Member();
        admin.setEmail("admin2@test.com");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setName("Admin User 2");
        admin.setPhone("0733333333");
        admin.setPersonId("19910101-1234");
        admin.setRole(Role.ADMIN);
        memberRepository.save(admin);

        String loginRequest = """
            {
                "username": "admin2@test.com",
                "password": "password123"
            }
            """;

        String response = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(response)
                .get("token")
                .asText();

        String memberRequest = """
            {
                "name": "New Librarian",
                "phone": "0744444444",
                "personId": "19960101-5678",
                "email": "newlibrarian@test.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/v1/member/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberRequest)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("newlibrarian@test.com"));
    }

// ===== EXCEPTION HANDLER TESTER =====

    @Test
    void createMemberWithMissingNameShouldReturn400() throws Exception {
        String memberRequest = """
            {
                "phone": "0711112222",
                "personId": "20000101-1234",
                "email": "noname@test.com",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createMemberWithMissingEmailShouldReturn400() throws Exception {
        String memberRequest = """
            {
                "name": "No Email",
                "phone": "0711112222",
                "personId": "20000101-1234",
                "password": "password123"
            }
            """;

        mockMvc.perform(post("/api/v1/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(memberRequest))
                .andExpect(status().isBadRequest());
    }
}