package com.pragma.powerup.trazabilidad.application.handler.impl;

import com.pragma.powerup.trazabilidad.application.dto.request.CreateOrderTraceRequestDto;
import com.pragma.powerup.trazabilidad.application.dto.response.OrderTraceResponseDto;
import com.pragma.powerup.trazabilidad.application.handler.IOrderTraceHandler;
import com.pragma.powerup.trazabilidad.application.mapper.ITrazabilidadDtoMapper;
import com.pragma.powerup.trazabilidad.domain.api.OrderTraceUseCasePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderTraceHandler implements IOrderTraceHandler {

    private final OrderTraceUseCasePort orderTraceUseCasePort;
    private final ITrazabilidadDtoMapper trazabilidadDtoMapper;

    @Override
    public void registrarEvento(CreateOrderTraceRequestDto requestDto) {
        orderTraceUseCasePort.registrarEvento(trazabilidadDtoMapper.toModel(requestDto));
    }

    @Override
    public List<OrderTraceResponseDto> obtenerTrazabilidadDePedido(Long idPedido, Long idClienteSolicitante) {
        return trazabilidadDtoMapper.toResponses(
                orderTraceUseCasePort.obtenerTrazabilidadDePedido(idPedido, idClienteSolicitante));
    }
}
