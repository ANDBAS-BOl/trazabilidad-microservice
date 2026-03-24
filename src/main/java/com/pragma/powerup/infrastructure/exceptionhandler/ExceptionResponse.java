package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    DOMAIN_ERROR("Business rule validation error"),
    UNAUTHORIZED_TRACE_ACCESS("No puedes consultar trazabilidad de pedidos de otro cliente"),
    INVALID_REQUEST("Solicitud invalida");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}