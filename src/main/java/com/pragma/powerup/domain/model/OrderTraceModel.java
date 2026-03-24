package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTraceModel {
    private String id;
    private Long idPedido;
    private Long idCliente;
    private Long idRestaurante;
    private Long idEmpleado;
    private String estadoAnterior;
    private String estadoNuevo;
    private Instant fechaEvento;
}
