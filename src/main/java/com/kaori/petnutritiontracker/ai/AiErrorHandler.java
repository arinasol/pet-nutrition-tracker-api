package com.kaori.petnutritiontracker.ai;

import com.google.genai.errors.ClientException;
import com.google.genai.errors.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class AiErrorHandler {

    public RuntimeException translate(RuntimeException exception) {

        Throwable cause = exception;

        while (cause != null) {

            if (cause instanceof ClientException clientException
                    && clientException.getMessage() != null
                    && clientException.getMessage().contains("429")) {

                return new AiServiceException(
                        HttpStatus.TOO_MANY_REQUESTS,
                        "AI request limit reached. Please try again later."
                );
            }

            if (cause instanceof ServerException serverException
                    && serverException.getMessage() != null
                    && serverException.getMessage().contains("503")) {

                return new AiServiceException(
                        HttpStatus.SERVICE_UNAVAILABLE,
                        "AI service is temporarily unavailable. Please try again later."
                );
            }

            cause = cause.getCause();
        }

        return exception;
    }
}