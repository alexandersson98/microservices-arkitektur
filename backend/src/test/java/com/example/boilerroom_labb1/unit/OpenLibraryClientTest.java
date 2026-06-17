package com.example.boilerroom_labb1.unit;


import com.example.boilerroom_labb1.client.OpenLibraryClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenLibraryClientTest {
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.Builder webClientBuilder;

    private OpenLibraryClient client;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(any())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
        client = new OpenLibraryClient(webClientBuilder);
    }

    @Test
    void shouldReturnDataFromOpenLibrary() {
        // mock chain: webClient.get().uri().retrieve().bodyToMono().block()
        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), anyString())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(Map.of("title", "Dune")));

        Map result = client.fetchByIsbn("9780441013593");

        assertThat(result).containsEntry("title", "Dune");
    }

    @Test
    void fallbackShouldReturnErrorMessage() {
        Map result = client.fallback("123", new RuntimeException("timeout"));

        assertThat(result).containsEntry("error", "Open Library are not available right now");
    }
}

