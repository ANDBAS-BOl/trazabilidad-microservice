package com.pragma.powerup.trazabilidad.domain.exception;

public class UnauthorizedTraceAccessException extends AccessDeniedException {
    public UnauthorizedTraceAccessException(String message) {
        super(message);
    }
}
