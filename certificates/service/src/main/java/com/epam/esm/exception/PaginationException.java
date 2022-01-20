package com.epam.esm.exception;

public class PaginationException extends RuntimeException {
    public PaginationException() {
        super();
    }

    public PaginationException(String message) {
        super(message);
    }

    public PaginationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaginationException(Throwable cause) {
        super(cause);
    }
}
