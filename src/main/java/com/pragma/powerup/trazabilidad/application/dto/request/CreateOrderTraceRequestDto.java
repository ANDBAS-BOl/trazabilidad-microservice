package com.pragma.powerup.trazabilidad.application.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record CreateOrderTraceRequestDto(
        @NotNull Long idPedido,
        @NotNull Long idCliente,
        @NotNull Long idRestaurante,
        Long idEmpleado,
        String estadoAnterior,
        @NotBlank String estadoNuevo
) {}
