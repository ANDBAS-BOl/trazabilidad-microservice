package com.pragma.powerup.trazabilidad.application.handler;

import com.pragma.powerup.trazabilidad.application.dto.request.CreateOrderTraceRequestDto;
import com.pragma.powerup.trazabilidad.application.dto.response.OrderTraceResponseDto;

import java.util.List;

public interface IOrderTraceHandler {
    void registrarEvento(CreateOrderTraceRequestDto requestDto);

    List<OrderTraceResponseDto> obtenerTrazabilidadDePedido(Long idPedido, Long idClienteSolicitante);
}
