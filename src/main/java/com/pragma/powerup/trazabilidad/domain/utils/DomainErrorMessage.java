package com.pragma.powerup.trazabilidad.domain.utils;

public enum DomainErrorMessage {

    REQUIRED_FIELDS("idPedido, idCliente e idRestaurante son obligatorios"),
    ESTADO_NUEVO_REQUIRED("El estadoNuevo es obligatorio"),
    ESTADO_ANTERIOR_REQUIRED("El estadoAnterior es obligatorio cuando el evento no es la creación del pedido"),

    UNAUTHORIZED_TRACE_ACCESS("No puedes consultar trazabilidad de pedidos de otro cliente"),

    TRACE_NOT_FOUND("No existen eventos de trazabilidad para el pedido indicado"),

    INVALID_REQUEST_BODY("El cuerpo de la solicitud es invalido o esta mal formado"),
    VALIDATION_FAILED("Los datos enviados no superan las validaciones de campo"),
    DATA_INTEGRITY_CONFLICT("El recurso entra en conflicto con datos ya existentes (posible duplicado)");

    private final String message;

    DomainErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
