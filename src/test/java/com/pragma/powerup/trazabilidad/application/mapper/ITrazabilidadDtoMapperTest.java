package com.pragma.powerup.trazabilidad.application.mapper;

import com.pragma.powerup.trazabilidad.application.dto.request.CreateOrderTraceRequestDto;
import com.pragma.powerup.trazabilidad.application.dto.response.OrderTraceResponseDto;
import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ITrazabilidadDtoMapperTest {

    private final ITrazabilidadDtoMapper mapper = new ITrazabilidadDtoMapperImpl();

    @Test
    void toModel_mapsDtoLeavingIdNullAndAutopopulatesFechaEvento() {
        CreateOrderTraceRequestDto dto = new CreateOrderTraceRequestDto(
                10L, 20L, 30L, 40L, "PENDIENTE", "EN_PREPARACION");

        OrderTraceModel model = mapper.toModel(dto);

        assertNull(model.getId());
        assertNotNull(model.getFechaEvento());
        assertEquals(10L, model.getIdPedido());
        assertEquals(20L, model.getIdCliente());
        assertEquals(30L, model.getIdRestaurante());
        assertEquals(40L, model.getIdEmpleado());
        assertEquals("PENDIENTE", model.getEstadoAnterior());
        assertEquals("EN_PREPARACION", model.getEstadoNuevo());
    }

    @Test
    void toResponse_preservesAllFields() {
        Instant fecha = Instant.parse("2024-01-15T12:00:00Z");
        OrderTraceModel model = OrderTraceModel.builder()
                .id("mongo-id-1")
                .idPedido(1L)
                .idCliente(2L)
                .idRestaurante(3L)
                .idEmpleado(4L)
                .estadoAnterior("A")
                .estadoNuevo("B")
                .fechaEvento(fecha)
                .build();

        OrderTraceResponseDto dto = mapper.toResponse(model);

        assertEquals(1L, dto.idPedido());
        assertEquals(2L, dto.idCliente());
        assertEquals(3L, dto.idRestaurante());
        assertEquals(4L, dto.idEmpleado());
        assertEquals("A", dto.estadoAnterior());
        assertEquals("B", dto.estadoNuevo());
        assertEquals(fecha, dto.fechaEvento());
    }

    @Test
    void toResponses_emptyList() {
        assertTrue(mapper.toResponses(List.of()).isEmpty());
    }

    @Test
    void toResponses_mapsEachElement() {
        OrderTraceModel m1 = OrderTraceModel.builder()
                .idPedido(1L).idCliente(1L).idRestaurante(1L)
                .estadoNuevo("LISTO")
                .fechaEvento(Instant.parse("2024-01-01T00:00:00Z"))
                .build();
        OrderTraceModel m2 = OrderTraceModel.builder()
                .idPedido(1L).idCliente(1L).idRestaurante(1L)
                .estadoNuevo("ENTREGADO")
                .fechaEvento(Instant.parse("2024-01-02T00:00:00Z"))
                .build();

        List<OrderTraceResponseDto> out = mapper.toResponses(List.of(m1, m2));

        assertEquals(2, out.size());
        assertEquals("LISTO", out.get(0).estadoNuevo());
        assertEquals("ENTREGADO", out.get(1).estadoNuevo());
    }
}
