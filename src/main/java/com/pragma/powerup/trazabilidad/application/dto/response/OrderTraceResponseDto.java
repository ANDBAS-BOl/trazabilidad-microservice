package com.pragma.powerup.trazabilidad.application.dto.response;

import java.time.Instant;

public record OrderTraceResponseDto(
        Long idPedido,
        Long idCliente,
        Long idRestaurante,
        Long idEmpleado,
        String estadoAnterior,
        String estadoNuevo,
        Instant fechaEvento
) {}
