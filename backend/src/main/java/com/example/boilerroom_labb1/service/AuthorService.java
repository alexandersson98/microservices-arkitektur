package com.example.boilerroom_labb1.service;

import com.example.boilerroom_labb1.dto.author.AuthorRequestDTO;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDTO;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDTO;
import com.example.boilerroom_labb1.entity.Author;
import com.example.boilerroom_labb1.exceptions.NotFoundWithIdException;
import com.example.boilerroom_labb1.exceptions.NotFoundException;
import com.example.boilerroom_labb1.mapper.AuthorMapper;
import com.example.boilerroom_labb1.mapper.BookMapper;
import com.example.boilerroom_labb1.repository.AuthorRepository;
import com.example.boilerroom_labb1.repository.BookRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


@Service
public class AuthorService {



    private final AuthorRepository repository;
    private final AuthorMapper mapper;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public AuthorService (AuthorRepository repository, AuthorMapper mapper, BookRepository bookRepository, BookMapper bookMapper){
        this.repository = repository;
        this.mapper = mapper;
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }


    @CacheEvict(value = "authors", allEntries = true)
    public AuthorResponseDTO createEntity(AuthorRequestDTO request){
        Author author = mapper.toEntity(request);
        repository.save(author);
         return mapper.toResponseDto(author);
    }

    @Cacheable("authors")
    public AuthorResponseDTO getAuthorById(Long id){
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new NotFoundWithIdException("Author not found with id: ", + id));
    }

        @Cacheable("book")
    public Page<BookResponseDTO> getBooksByAuthor(Long authorId, Pageable pageable) {

        if (!repository.existsById(authorId)) {
            throw new NotFoundException("Author not found");
        }

        return bookRepository.findByAuthorId(authorId, pageable)
                .map(bookMapper::toResponseDto);
    }
}
