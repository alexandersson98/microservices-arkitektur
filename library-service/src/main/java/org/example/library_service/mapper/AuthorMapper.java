package org.example.library_service.mapper;

import org.example.library_service.dto.author.AuthorRequestDTO;
import org.example.library_service.dto.author.AuthorResponseDTO;
import org.example.library_service.entity.Author;
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
