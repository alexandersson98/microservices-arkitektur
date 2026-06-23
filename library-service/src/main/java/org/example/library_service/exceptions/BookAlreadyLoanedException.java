package org.example.library_service.exceptions;

public class BookAlreadyLoanedException extends RuntimeException {
    public BookAlreadyLoanedException(String message) {
        super(message);
    }
}
