package org.example.library_service.exceptions;

public class NotFoundWithIdException extends RuntimeException {
    public NotFoundWithIdException(String message, Long id) {
        super(message + id);
    }
}
