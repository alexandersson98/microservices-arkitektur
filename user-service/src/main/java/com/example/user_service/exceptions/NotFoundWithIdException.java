package com.example.user_service.exceptions;

public class NotFoundWithIdException extends RuntimeException {
    public NotFoundWithIdException(String message, Long id) {
        super(message + id);
    }
}
