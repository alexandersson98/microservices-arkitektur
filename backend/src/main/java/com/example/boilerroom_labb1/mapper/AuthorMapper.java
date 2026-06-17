package com.example.boilerroom_labb1.mapper;

import com.example.boilerroom_labb1.dto.author.AuthorRequestDTO;
import com.example.boilerroom_labb1.dto.author.AuthorResponseDTO;
import com.example.boilerroom_labb1.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {


    private final BookMapper bookMapper;

    public AuthorMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public AuthorResponseDTO toResponseDto(Author author){
        return new AuthorResponseDTO(
                author.getId(),
                author.getName()
        );
    }



    public Author toEntity(AuthorRequestDTO request){
        return new Author(request.name());
    }
}
