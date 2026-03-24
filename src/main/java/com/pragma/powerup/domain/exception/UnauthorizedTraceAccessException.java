package com.pragma.powerup.domain.exception;

public class UnauthorizedTraceAccessException extends DomainException {
    public UnauthorizedTraceAccessException(String message) {
        super(message);
    }
}
