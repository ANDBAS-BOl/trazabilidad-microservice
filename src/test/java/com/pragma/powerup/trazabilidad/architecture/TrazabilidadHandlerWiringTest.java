package com.pragma.powerup.trazabilidad.architecture;

import com.pragma.powerup.trazabilidad.application.dto.request.CreateOrderTraceRequestDto;
import com.pragma.powerup.trazabilidad.application.dto.response.OrderTraceResponseDto;
import com.pragma.powerup.trazabilidad.application.handler.impl.OrderTraceHandler;
import com.pragma.powerup.trazabilidad.application.mapper.ITrazabilidadDtoMapper;
import com.pragma.powerup.trazabilidad.domain.api.OrderTraceUseCasePort;
import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrazabilidadHandlerWiringTest {

    @Mock
    private OrderTraceUseCasePort orderTraceUseCasePort;

    @Mock
    private ITrazabilidadDtoMapper trazabilidadDtoMapper;

    @InjectMocks
    private OrderTraceHandler orderTraceHandler;

    @Test
    void shouldDelegateRegistrarEventoToUseCaseAndMapper() {
        CreateOrderTraceRequestDto request = new CreateOrderTraceRequestDto(
                10L, 20L, 30L, 40L, "PENDIENTE", "EN_PREPARACION");
        OrderTraceModel model = OrderTraceModel.builder()
                .idPedido(10L)
                .idCliente(20L)
                .idRestaurante(30L)
                .idEmpleado(40L)
                .estadoAnterior("PENDIENTE")
                .estadoNuevo("EN_PREPARACION")
                .build();
        when(trazabilidadDtoMapper.toModel(request)).thenReturn(model);

        orderTraceHandler.registrarEvento(request);

        verify(trazabilidadDtoMapper).toModel(request);
        verify(orderTraceUseCasePort).registrarEvento(model);
    }

    @Test
    void shouldDelegateObtenerTrazabilidadToUseCaseAndMapper() {
        OrderTraceModel trace = OrderTraceModel.builder()
                .idPedido(1L)
                .idCliente(2L)
                .idRestaurante(3L)
                .estadoNuevo("LISTO")
                .fechaEvento(Instant.parse("2026-05-04T12:00:00Z"))
                .build();
        List<OrderTraceModel> models = List.of(trace);
        List<OrderTraceResponseDto> responses = List.of(
                new OrderTraceResponseDto(1L, 2L, 3L, null, null, "LISTO", trace.getFechaEvento()));
        when(orderTraceUseCasePort.obtenerTrazabilidadDePedido(eq(99L), eq(2L))).thenReturn(models);
        when(trazabilidadDtoMapper.toResponses(models)).thenReturn(responses);

        List<OrderTraceResponseDto> result = orderTraceHandler.obtenerTrazabilidadDePedido(99L, 2L);

        assertEquals(responses, result);
        verify(orderTraceUseCasePort).obtenerTrazabilidadDePedido(99L, 2L);
        verify(trazabilidadDtoMapper).toResponses(models);
    }
}
