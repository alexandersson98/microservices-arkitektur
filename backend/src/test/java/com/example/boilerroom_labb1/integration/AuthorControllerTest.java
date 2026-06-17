package com.example.boilerroom_labb1.integration;

import com.example.boilerroom_labb1.dto.author.AuthorResponseDTO;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;



public class AuthorControllerTest extends BaseIntegrationTest{

    @BeforeEach
    void setUp() {
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        setUpBase();
    }



    @Test
    void shouldCreateAuthor(){
        ResponseEntity<AuthorResponseDTO>postedAuthor = postAuthor("Matt Duffer");

        assertEquals(HttpStatus.CREATED, postedAuthor.getStatusCode());
        AuthorResponseDTO body = postedAuthor.getBody();
        assertNotNull(body);
        assertNotNull(body.id());
        assertEquals("Matt Duffer", body.name());
    }

    @Test
    void shouldReturnAuthorWithId(){

        ResponseEntity<AuthorResponseDTO>postedAuthor = postAuthor("Matt Duffer");
        Long authorId = postedAuthor.getBody().id();

        ResponseEntity<AuthorResponseDTO>getById = restTemplate.exchange("/api/v1/author/{id}",
                HttpMethod.GET,
                getRequest(),
                AuthorResponseDTO.class,
                authorId);

        assertEquals(HttpStatus.OK, getById.getStatusCode());

        AuthorResponseDTO fetchedBody = getById.getBody();
        assertNotNull(fetchedBody);
        assertEquals("Matt Duffer", fetchedBody.name());
        assertEquals(postedAuthor.getBody().id(), fetchedBody.id());
    }


    @Test
    void shouldReturnBooksByAuthorsId(){
        Long authorId =  createAuthor("Matt Duffer").id();
        ResponseEntity<BookResponseDTO> postedBook = postBook("Harry Potter",authorId, "eeee", 2007);

        ResponseEntity<Map> booksByAuthor = restTemplate.exchange(
                "/api/v1/author/{authorId}/books",
                HttpMethod.GET,
                getRequest(),
                Map.class,
                authorId
        );

        assertEquals(HttpStatus.OK, booksByAuthor.getStatusCode());
        assertNotNull(booksByAuthor.getBody());
        List<Map> content = (List<Map>) booksByAuthor.getBody().get("content");
        assertEquals(1, content.size());
        assertEquals("Harry Potter", content.get(0).get("title"));
    }

    @Test
    void shouldReturn404WhenNotFound(){
        Long id = 1L;
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/v1/author/{id}",
                HttpMethod.GET,
                getRequest(),
                String.class,
                id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
