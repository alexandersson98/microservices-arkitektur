package com.example.boilerroom_labb1.integration;

import com.example.boilerroom_labb1.dto.author.AuthorResponseDTO;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDTO;
import com.example.boilerroom_labb1.dto.loan.LoanResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorBookTest extends BaseIntegrationTest {

    @BeforeEach
    void setUp() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        setUpBase();
    }

    @Test
    void shouldCreateAuthorAndBook_andReturnBookViaAuthorEndpoint() {
        ResponseEntity<AuthorResponseDTO>postedAuthor = postAuthor("Gunnar Larsson");

        assertEquals(HttpStatus.CREATED, postedAuthor.getStatusCode());
        assertNotNull(postedAuthor.getBody());
        assertNotNull(postedAuthor.getBody().id());
        assertEquals("Gunnar Larsson", postedAuthor.getBody().name());
        Long authorId = postedAuthor.getBody().id();

        ResponseEntity<BookResponseDTO>postedBook = postBook("The hunger games", authorId, "edee-333", 2005);

        assertEquals(HttpStatus.CREATED, postedBook.getStatusCode());
        assertNotNull(postedAuthor.getBody());
        assertEquals("The hunger games", postedBook.getBody().title());
        assertEquals("edee-333", postedBook.getBody().isbn());
        assertEquals("Gunnar Larsson", postedBook.getBody().authorName());
        assertEquals(2005, postedBook.getBody().publishedYear());
    }

    @Test
    void shouldCreateAuthorBookAndLoan_AndReturnCreatedLoan() {

        Long authorId = createAuthor("Gunnar Larsson").id();
        Long bookId = createBook("The hunger games", authorId, "edee-333", 2005).id();
        ResponseEntity<LoanResponseDTO> postedLoan = postLoan(bookId);

        assertEquals(HttpStatus.CREATED, postedLoan.getStatusCode());
        assertNotNull(postedLoan.getBody());
        assertNotNull(postedLoan.getBody().id());

        Long createdLoanId = postedLoan.getBody().id();

        ResponseEntity<Map> loanResponseDto = restTemplate.exchange( "/api/v1/loans", HttpMethod.GET, getRequest(), Map.class);

        assertEquals(HttpStatus.OK, loanResponseDto.getStatusCode());
        assertNotNull(loanResponseDto);

        List<Map> content = (List<Map>) loanResponseDto.getBody().get("content");
        assertNotNull(content);

        Boolean found = content.stream()
                .anyMatch(loan -> createdLoanId.equals(((Number) loan.get("id")).longValue()));

        assertTrue(found);
    }
}
