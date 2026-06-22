package org.example.library_service.service;


import org.example.library_service.dto.author.AuthorRequestDTO;
import org.example.library_service.dto.author.AuthorResponseDTO;
import org.example.library_service.dto.book.v1.BookResponseDTO;
import org.example.library_service.entity.Author;
import org.example.library_service.exceptions.NotFoundException;
import org.example.library_service.exceptions.NotFoundWithIdException;
import org.example.library_service.mapper.AuthorMapper;
import org.example.library_service.mapper.BookMapper;
import org.example.library_service.repository.AuthorRepository;
import org.example.library_service.repository.BookRepository;
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
