package com.duckervn.cronservice.domain.exception;

public class InvalidCronExpression extends RuntimeException {
    public InvalidCronExpression() {
    }

    public InvalidCronExpression(String message) {
        super(message);
    }
}
