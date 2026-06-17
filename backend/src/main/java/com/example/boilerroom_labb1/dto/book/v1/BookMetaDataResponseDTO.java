package com.example.boilerroom_labb1.dto.book.v1;

import java.util.List;

public record BookMetaDataResponseDTO(
        String title,
        String fullTitle,
        String publishDate,
        List<String> publishers) {}
