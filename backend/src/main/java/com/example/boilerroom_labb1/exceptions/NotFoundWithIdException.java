package com.example.boilerroom_labb1.exceptions;

public class NotFoundWithIdException extends RuntimeException {
    public NotFoundWithIdException(String message, Long id) {
        super(message + id);
    }
}
