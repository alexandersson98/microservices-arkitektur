package org.example.library_service.dto.book.v1;

import java.util.List;

public record BookMetaDataResponseDTO(
        String title,
        String fullTitle,
        String publishDate,
        List<String> publishers) {}
