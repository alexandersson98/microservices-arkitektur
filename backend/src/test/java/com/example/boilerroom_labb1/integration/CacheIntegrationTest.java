package com.example.boilerroom_labb1.integration;

import com.example.boilerroom_labb1.dto.book.v1.BookResponseDTO;
import com.example.boilerroom_labb1.repository.LoanHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CacheIntegrationTest extends BaseIntegrationTest {

    @Autowired private CacheManager cacheManager;
    @Autowired private LoanHistoryRepository loanHistoryRepository;

    @BeforeEach
    void setUp() {
        loanHistoryRepository.deleteAll();
        loanRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
        setUpBase();
    }


    @Test
    void shouldPopulateBookCacheAfterGetById() {
        Long authorId = createAuthor("Test Author").id();
        Long bookId = createBook("Test Book", authorId,"1234", 2000).id();

        restTemplate.exchange("/api/v1/books/" + bookId, HttpMethod.GET, getRequest(), BookResponseDTO.class);

        Cache bookCache = cacheManager.getCache("book");
        assertThat(bookCache.get(bookId)).isNotNull();
    }

    @Test
    void shouldEvictBookCacheAfterCreatingBook() {
        Long authorId = createAuthor("Test Author").id();
        Long bookId = createBook( "Test Book", authorId,"1234", 2000).id();

        restTemplate.exchange("/api/v1/books/" + bookId, HttpMethod.GET, getRequest(), BookResponseDTO.class);
        assertThat(cacheManager.getCache("book").get(bookId)).isNotNull();

        createBook("Another Book", authorId,"5678", 2001);

        assertThat(cacheManager.getCache("book").get(bookId)).isNull();
    }

    @Test
    void shouldEvictLoanCacheAfterCreatingLoan() {
        Long authorId = createAuthor("Test Author").id();
        Long bookId1 = createBook("Test Book", authorId,"1234", 2000).id();
        Long bookId2 = createBook("Another Book", authorId,"5678", 2001).id();

        createLoan(bookId1);
        restTemplate.exchange("/api/v1/loans", HttpMethod.GET, getRequest(), Object.class);

        Cache loanCache = cacheManager.getCache("loan");
        Pageable defaultPageable = PageRequest.of(0, 20);
        assertThat(loanCache.get(defaultPageable)).isNotNull();

        createLoan(bookId2);

        assertThat(loanCache.get(defaultPageable)).isNull();
    }
}
