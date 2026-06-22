package org.example.library_service.mapper;


import org.example.library_service.dto.author.AuthorResponseDTO;
import org.example.library_service.dto.book.BookRequestDTO;
import org.example.library_service.dto.book.v1.BookMetaDataResponseDTO;
import org.example.library_service.dto.book.v1.BookResponseDTO;
import org.example.library_service.dto.book.v2.BookResponseDtoV2;
import org.example.library_service.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BookMapper {
    public BookResponseDTO toResponseDto
            (Book book){
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor().getName(),
                book.getIsbn(),
                book.getPublishedYear(),
        book.getVersion());

    }

    public Book toEntity(BookRequestDTO request){
          Book book = new Book();
                book.setTitle(request.title());
                book.setIsbn(request.isbn());
                book.setPublishedYear(request.publishedYear());
                return book;
    }

    public BookResponseDtoV2 toResponseDtoV2(Book book, Boolean available){
        return new BookResponseDtoV2(
                book.getId(),
                book.getTitle(),
                new AuthorResponseDTO(book.getAuthor().getId(),
                        book.getAuthor().getName()),
                book.getIsbn(),
                book.getPublishedYear(),
                available
                );
    }
    public BookMetaDataResponseDTO bookMetaDataResponseDTO(Map data){
        return new BookMetaDataResponseDTO(
                (String) data.get("title"),
                (String) data.get("full_title"),
                (String) data.get("publish_date"),
                (List<String>) data.get("publishers"));

    }
}
