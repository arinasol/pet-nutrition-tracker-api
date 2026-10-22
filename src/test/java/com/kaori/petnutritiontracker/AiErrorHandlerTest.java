package com.kaori.petnutritiontracker;

import com.google.genai.errors.ClientException;
import com.google.genai.errors.ServerException;
import com.kaori.petnutritiontracker.ai.AiErrorHandler;
import com.kaori.petnutritiontracker.ai.AiServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AiErrorHandlerTest {

    private AiErrorHandler aiErrorHandler;

    @BeforeEach
    void setUp() {
        aiErrorHandler = new AiErrorHandler();
    }

    @Test
    void shouldTranslate429ToTooManyRequests() {
        ClientException googleException = mock(ClientException.class);

        when(googleException.getMessage())
                .thenReturn("429 Quota exceeded");

        RuntimeException wrappedException =
                new RuntimeException("Failed to generate content", googleException);

        RuntimeException result = aiErrorHandler.translate(wrappedException);

        assertInstanceOf(AiServiceException.class, result);

        AiServiceException aiException = (AiServiceException) result;

        assertEquals(HttpStatus.TOO_MANY_REQUESTS, aiException.getStatus());
        assertEquals(
                "AI request limit reached. Please try again later.",
                aiException.getMessage()
        );
    }

    @Test
    void shouldTranslate503ToServiceUnavailable() {
        ServerException googleException = mock(ServerException.class);

        when(googleException.getMessage())
                .thenReturn("503 Model is experiencing high demand");

        RuntimeException wrappedException =
                new RuntimeException("Failed to generate content", googleException);

        RuntimeException result = aiErrorHandler.translate(wrappedException);

        assertInstanceOf(AiServiceException.class, result);

        AiServiceException aiException = (AiServiceException) result;

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, aiException.getStatus());
    }

    @Test
    void shouldKeepUnknownExceptionsUnchanged() {
        RuntimeException original =
                new RuntimeException("Unexpected error");

        RuntimeException result = aiErrorHandler.translate(original);

        assertSame(original, result);
    }
}