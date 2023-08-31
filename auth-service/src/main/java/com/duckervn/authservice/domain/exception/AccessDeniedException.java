package com.duckervn.authservice.domain.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException() {
    }

    public AccessDeniedException(String message) {
        super(message);
    }
}
