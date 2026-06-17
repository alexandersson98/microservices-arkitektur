package com.example.boilerroom_labb1.integration;

import com.example.boilerroom_labb1.dto.author.AuthorResponseDTO;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDTO;
import com.example.boilerroom_labb1.dto.loan.LoanHistoryResponseDTO;
import com.example.boilerroom_labb1.dto.loan.LoanResponseDTO;
import com.example.boilerroom_labb1.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class LoanControllerTest extends BaseIntegrationTest{

    @Autowired
    private LoanHistoryRepository loanHistoryRepository;



    @BeforeEach
    void setUp() {
        loanHistoryRepository.deleteAll();
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        setUpBase();
    }

    @Test
    void shouldCreateLoan() {
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

        BookResponseDTO createdBook = createBook("Stranger things", authorId, "eeee", 2007);
        Long bookId = createdBook.id();

        ResponseEntity<LoanResponseDTO> postedLoan = postLoan(bookId);

        assertEquals(HttpStatus.CREATED, postedLoan.getStatusCode());
        assertNotNull(postedLoan.getBody());
        assertNotNull(postedLoan.getBody().id());
    }

    @Test
    void shouldReturnAllActiveLoans() {
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

        BookResponseDTO createdBook = createBook("Stranger things", authorId, "eeee", 2007);
        Long bookId = createdBook.id();

        ResponseEntity<LoanResponseDTO> postedLoan = postLoan(bookId);


        ResponseEntity<Map> activeLoans = restTemplate.exchange("/api/v1/loans",
                HttpMethod.GET,
                getRequest(),
                Map.class);

        assertEquals(HttpStatus.CREATED, postedLoan.getStatusCode());
        assertEquals(HttpStatus.OK, activeLoans.getStatusCode());

        List<Map> content = (List<Map>) activeLoans.getBody().get("content");
        assertNotNull(content);
        assertTrue(content.size() > 0);
        assertNotNull(content.get(0));
    }

    @Test
    void shouldReturn404WhenCreateLoanAndBookNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        Long bookId = 999L;
        ResponseEntity<LoanResponseDTO> postedLoan = postLoan(bookId);

        assertEquals(HttpStatus.NOT_FOUND, postedLoan.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestWhenTryingToLoanSameBookTwice(){
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

        BookResponseDTO createdBook = createBook("Stranger things", authorId, "eeee", 2007);
        Long bookId = createdBook.id();

        ResponseEntity<LoanResponseDTO> postedLoan = postLoan(bookId);


        ResponseEntity<LoanResponseDTO> loanResponse2 = postLoan(bookId);

        assertEquals(HttpStatus.CREATED, postedLoan.getStatusCode());
        assertEquals(HttpStatus.CONFLICT, loanResponse2.getStatusCode());
    }

    @Test
    void shouldAllowOnlyOneLoanWhenConcurrentRequests()throws Exception{
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

        BookResponseDTO createdBook = createBook("Stranger things", authorId, "eeee", 2007);
        Long bookId = createdBook.id();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(2);

        Callable<ResponseEntity<LoanResponseDTO>> task = () -> {
            try {
                startLatch.await();


            return postLoan(bookId);

        }finally {
                doneLatch.countDown();
            }
        };

            Future<ResponseEntity<LoanResponseDTO>> future1 = executor.submit(task);
            Future<ResponseEntity<LoanResponseDTO>> future2 = executor.submit(task);
        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();


       List<ResponseEntity<LoanResponseDTO>>responses = Stream.of(future1, future2)
                       .map(f -> {
                           try { return f.get(); }
                           catch (Exception e) { throw new RuntimeException(e); }
                               })
                               .toList();

       assertThat(loanRepository.count()).isEqualTo(1);

       assertThat(responses.stream()
               .filter(f -> f.getStatusCode() == HttpStatus.CREATED)
               .count()).isEqualTo(1);

       assertThat(responses.stream()
               .filter(f -> f.getStatusCode() == HttpStatus.CONFLICT)
               .count()).isEqualTo(1);

        }

    @Test
    void  shouldReturnBookAndMoveToLoanHistory(){
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

        BookResponseDTO createdBook = createBook("Stranger things", authorId, "eeee", 2007);
        Long bookId = createdBook.id();

        LoanResponseDTO createLoan = createLoan(bookId);
        Long loanId = createLoan.id();

        ResponseEntity<LoanHistoryResponseDTO> returnResponse = restTemplate.exchange(
                "/api/v1/loans/" + loanId,
                HttpMethod.PATCH,
                getRequest(),
                LoanHistoryResponseDTO.class);

        assertEquals(HttpStatus.OK, returnResponse.getStatusCode());
        assertNotNull(returnResponse.getBody());
        assertEquals("Book has been returned", returnResponse.getBody().message());
        assertThat(loanRepository.count()).isEqualTo(0);
        assertThat(loanHistoryRepository.count()).isEqualTo(1);
    }
    @Test
    void shouldReturn400WhenBookAlreadyReturned(){
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

        BookResponseDTO createdBook = createBook("Stranger things", authorId, "eeee", 2007);
        Long bookId = createdBook.id();

        LoanResponseDTO createLoan = createLoan(bookId);
        Long loanId = createLoan.id();

        restTemplate.exchange("/api/v1/loans/" + loanId, HttpMethod.PATCH, getRequest(), LoanHistoryResponseDTO.class);

        ResponseEntity<String> returnAgain = restTemplate.exchange(
                "/api/v1/loans/" + loanId,
                HttpMethod.PATCH,
                getRequest(),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, returnAgain.getStatusCode());
    }

    @Test
    void demonstratesRaceConditionProblem() throws Exception {
        // utan locking kan 2 trådar läsa boken samtidigt och båda ser den som ledig
        // då skapas 2 lån på samma bok vilket är fel
        // med pessimistic locking låses raden i databasen så bara en tråd åt gången kan läsa
        // testet shouldAllowOnlyOneLoanWhenConcurrentRequests visar att lösningen fungerar
    }

    @Test
    void shouldAllowOnlyOneLoanOutOf100ConcurrentRequests() throws Exception {
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

        BookResponseDTO createdBook = createBook("Stranger things", authorId, "eeee", 2007);
        Long bookId = createdBook.id();

        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        List<Future<ResponseEntity<LoanResponseDTO>>> futures = new java.util.ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                try {
                    startLatch.await();

                    return postLoan(bookId);
                } finally {
                    doneLatch.countDown();
                }
            }
            ));
        }

        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        List<ResponseEntity<LoanResponseDTO>> responses = futures.stream()
                .map(f -> {
                    try { return f.get(); }
                    catch (Exception e) { throw new RuntimeException(e); }
                })
                .toList();

        assertThat(loanRepository.count()).isEqualTo(1);

        assertThat(responses.stream()
                .filter(r -> r.getStatusCode() == HttpStatus.CREATED)
                .count()).isEqualTo(1);

            assertThat(responses.stream()
                .filter(r -> r.getStatusCode() == HttpStatus.CONFLICT)
                .count()).isEqualTo(99);
    }
}
