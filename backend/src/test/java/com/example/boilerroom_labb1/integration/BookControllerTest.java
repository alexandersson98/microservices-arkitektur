package com.example.boilerroom_labb1.integration;

import com.example.boilerroom_labb1.dto.author.AuthorResponseDTO;
import com.example.boilerroom_labb1.dto.book.BookRequestDTO;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDTO;
import com.example.boilerroom_labb1.dto.book.v1.EditBookRequestDTO;
import com.example.boilerroom_labb1.exceptions.handler.ApiErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;


import static org.assertj.core.api.Assertions.assertThat;




    public class BookControllerTest extends BaseIntegrationTest {


    @BeforeEach void setUp(){
    bookRepository.deleteAll();
    authorRepository.deleteAll();
    memberRepository.deleteAll();
    setUpBase();
}


    @Test
    void shouldReturn201AndSaveBookWhenCreatingValidBook(){

        ResponseEntity<AuthorResponseDTO> createdAuthor = postAuthor("Matt Duffer");
        Long authorId = createdAuthor.getBody().id();

        ResponseEntity<BookResponseDTO> postedBook = postBook("Stranger Things", authorId, "EV443-FRed", 2016);

        assertThat(postedBook.getStatusCode().value()).isEqualTo(201);
        assertThat(postedBook.getBody()).isNotNull();
        assertThat(bookRepository.count()).isEqualTo(1);

        assertThat(postedBook.getBody().title()).isEqualTo("Stranger Things");
        assertThat(postedBook.getBody().authorName()).isEqualTo("Matt Duffer");
        assertThat(postedBook.getBody().isbn()).isEqualTo("EV443-FRed");
        assertThat(postedBook.getBody().publishedYear()).isEqualTo(2016);
        }

    @Test
    void shouldReturn200AndBookWhenBookExists() {
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();
        ResponseEntity<BookResponseDTO>postedBook = postBook("Stranger Things", authorId, "Test123", 2016);
        Long bookId = postedBook.getBody().id();

        ResponseEntity<BookResponseDTO> getBookWithId = restTemplate.exchange("/api/v1/books/{id}",
                HttpMethod.GET,
                getRequest(),
                BookResponseDTO.class,
                bookId
                );

        assertThat(getBookWithId.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getBookWithId).isNotNull();

        assertThat(getBookWithId.getBody().title()).isEqualTo("Stranger Things");
        assertThat(getBookWithId.getBody().authorName()).isEqualTo("Matt Duffer");
    }

    @Test
    void shouldThrowValidationException(){
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

        HttpEntity<BookRequestDTO>bookRequest = getBookRequest("Stranger Things",
                authorId,
                "EV443-FRed",
                900);

        ResponseEntity<ApiErrorResponse> errorResponse = restTemplate.exchange("/api/v1/books",
                HttpMethod.POST,
                bookRequest,
                ApiErrorResponse.class);

       assertThat(errorResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
       assertThat(errorResponse.getBody()).isNotNull();
       assertThat(errorResponse.getBody().message().contains("Published year must be greater than 1700"));

    }

    @Test
    void shouldReturn200AndUpdatedBookWhenEditingBook() {
        AuthorResponseDTO createdAuthor = createAuthor("Matt Duffer");
        Long authorId = createdAuthor.id();

       AuthorResponseDTO newCreatedAuthor = createAuthor("J.K Rowling");
        Long newAuthorId = newCreatedAuthor.id();

        ResponseEntity<BookResponseDTO>postedBook = postBook( "Stranger Things",
                authorId,
                "EV443-FRed",
                2016);

        Long bookId = postedBook.getBody().id();

        HttpEntity<EditBookRequestDTO>editBookRequest = new HttpEntity<>(new EditBookRequestDTO(
                "Stranger Things 2", newAuthorId , "ISBN", 2022), getAuthHeaders());


        ResponseEntity<BookResponseDTO> editResponse = restTemplate.exchange(
                "/api/v1/books/edit/" + bookId,
                HttpMethod.PATCH,
                editBookRequest,
                BookResponseDTO.class
        );

        assertThat(editResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(editResponse.getBody()).isNotNull();
        assertThat(editResponse.getBody().title()).isEqualTo("Stranger Things 2");
        assertThat(editResponse.getBody().authorName()).isEqualTo("J.K Rowling");
        assertThat(editResponse.getBody().isbn()).isEqualTo("ISBN");
        assertThat(editResponse.getBody().publishedYear()).isEqualTo(2022);
    }
}


