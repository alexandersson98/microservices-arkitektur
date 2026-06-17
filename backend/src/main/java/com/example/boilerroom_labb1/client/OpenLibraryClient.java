package com.example.boilerroom_labb1.client;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class OpenLibraryClient {

    private final WebClient webClient;


    public OpenLibraryClient(WebClient.Builder builder){
        this.webClient = builder.baseUrl("https://openlibrary.org/").build();
    }


    @CircuitBreaker(name = "openLibrary", fallbackMethod ="fallback")
    public Map fetchByIsbn(String isbn){
        return   webClient.get()
                .uri("/isbn/{isbn}.json", isbn)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }

    public Map fallback(String isbn, Exception e) {
        return Map.of("error", "Open Library are not available right now");
    }
}
