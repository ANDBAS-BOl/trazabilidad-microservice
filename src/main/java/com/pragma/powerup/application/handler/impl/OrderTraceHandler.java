package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.CreateOrderTraceRequestDto;
import com.pragma.powerup.application.dto.response.OrderTraceResponseDto;
import com.pragma.powerup.application.handler.IOrderTraceHandler;
import com.pragma.powerup.domain.api.IOrderTraceServicePort;
import com.pragma.powerup.domain.model.OrderTraceModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTraceHandler implements IOrderTraceHandler {

    private final IOrderTraceServicePort orderTraceServicePort;

    @Override
    public void registrarEvento(CreateOrderTraceRequestDto requestDto) {
        OrderTraceModel model = OrderTraceModel.builder()
                .idPedido(requestDto.getIdPedido())
                .idCliente(requestDto.getIdCliente())
                .idRestaurante(requestDto.getIdRestaurante())
                .idEmpleado(requestDto.getIdEmpleado())
                .estadoAnterior(requestDto.getEstadoAnterior())
                .estadoNuevo(requestDto.getEstadoNuevo())
                .build();
        orderTraceServicePort.registrarEvento(model);
    }

    @Override
    public List<OrderTraceResponseDto> obtenerTrazabilidadDePedido(Long idPedido, Long idClienteSolicitante) {
        return orderTraceServicePort.obtenerTrazabilidadDePedido(idPedido, idClienteSolicitante)
                .stream()
                .map(model -> OrderTraceResponseDto.builder()
                        .idPedido(model.getIdPedido())
                        .idCliente(model.getIdCliente())
                        .idRestaurante(model.getIdRestaurante())
                        .idEmpleado(model.getIdEmpleado())
                        .estadoAnterior(model.getEstadoAnterior())
                        .estadoNuevo(model.getEstadoNuevo())
                        .fechaEvento(model.getFechaEvento())
                        .build())
                .toList();
    }
}
