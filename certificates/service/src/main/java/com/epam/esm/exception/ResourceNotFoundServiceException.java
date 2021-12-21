package com.epam.esm.exception;

public class ResourceNotFoundServiceException extends Exception {
    public ResourceNotFoundServiceException() {
        super();
    }

    public ResourceNotFoundServiceException(String message) {
        super(message);
    }

    public ResourceNotFoundServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotFoundServiceException(Throwable cause) {
        super(cause);
    }
}
