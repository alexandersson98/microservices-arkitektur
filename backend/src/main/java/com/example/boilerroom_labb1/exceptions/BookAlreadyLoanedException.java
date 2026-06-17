package com.example.boilerroom_labb1.exceptions;

public class BookAlreadyLoanedException extends RuntimeException {
    public BookAlreadyLoanedException(String message) {
        super(message);
    }
}
