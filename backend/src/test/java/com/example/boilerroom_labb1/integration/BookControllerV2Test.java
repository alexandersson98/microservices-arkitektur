package com.example.boilerroom_labb1.integration;

import com.example.boilerroom_labb1.dto.author.AuthorResponseDTO;
import com.example.boilerroom_labb1.dto.book.BookRequestDTO;
import com.example.boilerroom_labb1.dto.book.v2.BookResponseDtoV2;
import com.example.boilerroom_labb1.dto.book.v2.BookWrapperGetByIdDtoV2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import static org.assertj.core.api.Assertions.assertThat;


public class BookControllerV2Test extends BaseIntegrationTest {



    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        setUpBase();
    }

    @Test
    void shouldCreateBookAndReturnV2Response() {
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();
        String authorName = createdAuthor.name();

        HttpEntity<BookRequestDTO> bookRequest = getBookRequest("Stranger Things", authorId, "EV443-FRed", 2016);

        ResponseEntity<BookResponseDtoV2> bookResponse = restTemplate.
        exchange("/api/v2/books",
                HttpMethod.POST,
                bookRequest,
                BookResponseDtoV2.class);
        assertThat(bookResponse.getStatusCode().value()).isEqualTo(201);
        assertThat(bookResponse.getBody()).isNotNull();
        assertThat(bookResponse.getBody().title()).isEqualTo("Stranger Things");
        assertThat(bookResponse.getBody().author().name()).isEqualTo(authorName);
        assertThat(bookResponse.getBody().author().id()).isEqualTo(authorId);
        assertThat(bookResponse.getBody().isbn()).isEqualTo("EV443-FRed");
        assertThat(bookResponse.getBody().publishedYear()).isEqualTo(2016);
    }

    @Test
    void shouldReturnV2BookResponseWhenBookExists() {
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

        HttpEntity<BookRequestDTO>bookRequest = getBookRequest(
                "Stranger Things",
                authorId,
                "EV443-FRed",
                2016);

        ResponseEntity<BookResponseDtoV2> created = restTemplate.exchange("/api/v2/books",
                HttpMethod.POST,
                bookRequest,
                BookResponseDtoV2.class);

        Long bookId = created.getBody().id();

        ResponseEntity<BookWrapperGetByIdDtoV2> response = restTemplate.exchange("/api/v2/books/" + bookId,
                HttpMethod.GET,
                bookRequest,
                BookWrapperGetByIdDtoV2.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().version()).isEqualTo("V2");



        BookResponseDtoV2 book = response.getBody().data();

        assertThat(book.title()).isEqualTo("Stranger Things");
        assertThat(book.author().id()).isEqualTo(authorId);
    }
}