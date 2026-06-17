package com.example.boilerroom_labb1.service;

import com.example.boilerroom_labb1.client.OpenLibraryClient;
import com.example.boilerroom_labb1.dto.book.v1.BookMetaDataResponseDTO;
import com.example.boilerroom_labb1.dto.book.v1.EditBookRequestDTO;
import com.example.boilerroom_labb1.dto.book.BookRequestDTO;
import com.example.boilerroom_labb1.dto.book.v1.BookResponseDTO;
import com.example.boilerroom_labb1.dto.book.v2.BookResponseDtoV2;
import com.example.boilerroom_labb1.dto.book.v2.BookWrapperDtoV2;
import com.example.boilerroom_labb1.dto.book.v2.BookWrapperGetByIdDtoV2;
import com.example.boilerroom_labb1.entity.Author;
import com.example.boilerroom_labb1.entity.Book;
import com.example.boilerroom_labb1.exceptions.NotFoundWithIdException;
import com.example.boilerroom_labb1.exceptions.ValidationException;
import com.example.boilerroom_labb1.mapper.BookMapper;
import com.example.boilerroom_labb1.repository.AuthorRepository;
import com.example.boilerroom_labb1.repository.BookRepository;
import com.example.boilerroom_labb1.repository.LoanRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BookService {
    private final BookRepository repository;
    private final BookMapper mapper;
    private final AuthorRepository authorRepository;
    private final LoanRepository loanRepository;
    private final OpenLibraryClient openLibraryClient;


    public BookService(BookRepository repository,
    BookMapper mapper, AuthorRepository authorRepository, LoanRepository loanRepository, OpenLibraryClient openLibraryClient){
        this.repository = repository;
        this.mapper = mapper;
        this.authorRepository = authorRepository;
        this.loanRepository = loanRepository;
        this.openLibraryClient = openLibraryClient;

    }

    public Book createEntity(BookRequestDTO request){
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new NotFoundWithIdException("Author not found with id: ", request.authorId()));

        Book book = mapper.toEntity(request);
        book.setAuthor(author);
        if (request.publishedYear() <= 1700) {
            throw new ValidationException("Published year must be greater than 1700");
        }
        return repository.save(book);
    }

    @CacheEvict(value = "book", allEntries = true)
    public BookResponseDTO createBook(BookRequestDTO request){
        Book saved = createEntity(request);
        return mapper.toResponseDto(saved);
    }

    @CacheEvict(value = "book", allEntries = true)
    public BookResponseDtoV2 createBookV2(BookRequestDTO request){
        Book saved = createEntity(request);
        return mapper.toResponseDtoV2(saved, !loanRepository.existsByBookId(saved.getId()));
    }

    @Cacheable("book")
    public BookResponseDTO getBookById(Long id){
        return repository.findById(id)
                .map(mapper::toResponseDto)
                .orElseThrow(() -> new NotFoundWithIdException("Book not found with id: ", + id));
    }

    @Cacheable("book")
    public BookWrapperGetByIdDtoV2 getBookByIdV2(Long id) {
        BookResponseDtoV2 dto  = repository.findById(id)
                .map(book -> mapper.toResponseDtoV2(book, !loanRepository.existsByBookId(book.getId())))
                .orElseThrow(() -> new NotFoundWithIdException("Book not found with id: ", + id));
        return new BookWrapperGetByIdDtoV2(dto, "V2");
    }
    @Cacheable("book")
    public BookWrapperDtoV2 getAllV2(Pageable pageable) {
        Page<BookResponseDtoV2> books = repository.findAll(pageable)
                .map(book -> mapper.toResponseDtoV2(book, !loanRepository.existsByBookId(book.getId())));

        return new BookWrapperDtoV2(books, "V2");
    }
    @Cacheable("book")
    public Page<BookResponseDTO> getAll(Pageable pageable){
        Page<Book>all = repository.findAll(pageable);
        return all.map(mapper::toResponseDto);

    }

 @CacheEvict(value = "book", allEntries = true)
    public BookResponseDTO editBook(Long id, EditBookRequestDTO editBookRequest){
        Book book = repository.findById(id).orElseThrow(() -> new NotFoundWithIdException("Book not found with id: ", + id));
            if(editBookRequest.title() != null){
                book.setTitle(editBookRequest.title());
            }
            if (editBookRequest.authorId() != null ){
                book.setAuthor(authorRepository.findById(editBookRequest.authorId()).orElseThrow(()->new NotFoundWithIdException("Author not found with id: ", + editBookRequest.authorId())));
            }
            if (editBookRequest.isbn() != null) {
                book.setIsbn(editBookRequest.isbn());
            }
            if (editBookRequest.publishedYear() != null) {
                book.setPublishedYear(editBookRequest.publishedYear());
            }
            return mapper.toResponseDto(repository.save(book));
    }

    public BookMetaDataResponseDTO getBookMetaData(Long id){
        Book book = repository.findById(id).orElseThrow();
        Map data = openLibraryClient.fetchByIsbn(book.getIsbn());
        return mapper.bookMetaDataResponseDTO(data);


    }
}
