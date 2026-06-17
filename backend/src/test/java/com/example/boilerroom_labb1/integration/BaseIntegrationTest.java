package com.example.boilerroom_labb1.integration;

import com.example.boilerroom_labb1.dto.auth.LoginRequestDTO;
import com.example.boilerroom_labb1.dto.auth.LoginResponseDTO;
import com.example.boilerroom_labb1.dto.author.AuthorRequestDTO;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDTO;
import com.example.boilerroom_labb1.dto.book.BookRequestDTO;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDTO;
import com.example.boilerroom_labb1.dto.loan.LoanRequestDTO;
import com.example.boilerroom_labb1.dto.loan.LoanResponseDTO;
import com.example.boilerroom_labb1.dto.member.MemberRequestDTO;
import com.example.boilerroom_labb1.repository.AuthorRepository;
import com.example.boilerroom_labb1.repository.BookRepository;
import com.example.boilerroom_labb1.repository.LoanRepository;
import com.example.boilerroom_labb1.repository.MemberRepository;
import com.example.boilerroom_labb1.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;
    @Autowired
    protected BookRepository bookRepository;
    @Autowired
    protected AuthorRepository authorRepository;
    @Autowired
    protected LoanRepository loanRepository;
    @Autowired
    protected MemberService memberService;
    @Autowired
    protected MemberRepository memberRepository;
    protected String adminToken;


    protected void setUpBase() {
        memberRepository.deleteAll();
        memberService.createAdmin(new MemberRequestDTO("Lukas", "0723232323", "19980723-2315", "lukas@gmail.com", "Password123!"));

        LoginResponseDTO loginResponse = restTemplate.postForEntity(
                "/api/v1/auth/login",
                new LoginRequestDTO("lukas@gmail.com", "Password123!"),
                LoginResponseDTO.class
        ).getBody();
        adminToken = loginResponse.token();
    }

    protected HttpHeaders getAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        return headers;
    }

    protected HttpEntity<Void> getRequest(){
         return new HttpEntity<>(null, getAuthHeaders());
    }


    protected HttpEntity<AuthorRequestDTO> getAuthorRequest(String name) {
        return new HttpEntity<>(new AuthorRequestDTO(name), getAuthHeaders());
    }

    protected ResponseEntity<AuthorResponseDTO> postAuthor(String name) {
        return restTemplate.exchange("/api/v1/author", HttpMethod.POST, getAuthorRequest(name), AuthorResponseDTO.class);
    }

    protected AuthorResponseDTO createAuthor(String name) {
        return postAuthor(name).getBody();
    }






    protected HttpEntity<BookRequestDTO> getBookRequest(String title, Long authorId, String isbn, int year) {
        return new HttpEntity<>(new BookRequestDTO(title, authorId, isbn, year), getAuthHeaders());
    }

    protected ResponseEntity<BookResponseDTO> postBook(String title, Long authorId, String isbn, int year) {
        return restTemplate.exchange("/api/v1/books", HttpMethod.POST, getBookRequest(title, authorId, isbn, year), BookResponseDTO.class);
    }

    protected BookResponseDTO createBook(String title, Long authorId, String isbn, int year) {
        return postBook(title, authorId, isbn, year).getBody();
    }






    protected HttpEntity<LoanRequestDTO> getLoanRequest(Long bookId) {
        return new HttpEntity<>(new LoanRequestDTO(bookId), getAuthHeaders());
    }

    protected ResponseEntity<LoanResponseDTO> postLoan(Long bookId) {
        return restTemplate.exchange("/api/v1/loans", HttpMethod.POST, getLoanRequest(bookId), LoanResponseDTO.class);
    }

    protected LoanResponseDTO createLoan(Long bookId) {
        return postLoan(bookId).getBody();
    }
}
