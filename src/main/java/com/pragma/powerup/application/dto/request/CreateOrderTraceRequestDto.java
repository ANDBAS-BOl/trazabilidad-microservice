package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CreateOrderTraceRequestDto {
    @NotNull
    private Long idPedido;
    @NotNull
    private Long idCliente;
    @NotNull
    private Long idRestaurante;
    private Long idEmpleado;
    private String estadoAnterior;
    @NotBlank
    private String estadoNuevo;
}
