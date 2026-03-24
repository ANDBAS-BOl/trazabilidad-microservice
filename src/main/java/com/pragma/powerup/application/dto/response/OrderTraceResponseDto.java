package com.pragma.powerup.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
public class OrderTraceResponseDto {
    private Long idPedido;
    private Long idCliente;
    private Long idRestaurante;
    private Long idEmpleado;
    private String estadoAnterior;
    private String estadoNuevo;
    private Instant fechaEvento;
}
