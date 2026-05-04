package com.pragma.powerup.trazabilidad.domain.usecase;

import com.pragma.powerup.trazabilidad.domain.exception.UnauthorizedTraceAccessException;
import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;
import com.pragma.powerup.trazabilidad.domain.spi.OrderTracePersistencePort;
import com.pragma.powerup.trazabilidad.domain.utils.DomainErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class OrderTraceUseCaseTest {

    private OrderTracePersistencePort persistencePort;
    private OrderTraceUseCase useCase;

    @BeforeEach
    void setUp() {
        persistencePort = mock(OrderTracePersistencePort.class);
        useCase = new OrderTraceUseCase(persistencePort);
    }

    @Test
    void registrarEventoDelegaAlPuerto() {
        OrderTraceModel model = sampleModel();

        useCase.registrarEvento(model);

        verify(persistencePort).guardarEvento(model);
        verifyNoMoreInteractions(persistencePort);
    }

    @Test
    void obtenerTrazabilidadOrdenadaParaDuenio() {
        Long idPedido = 99L;
        Long idCliente = 1L;
        Instant t1 = Instant.parse("2024-06-01T12:00:00Z");
        Instant t2 = Instant.parse("2024-06-02T12:00:00Z");
        Instant t3 = Instant.parse("2024-06-03T12:00:00Z");
        List<OrderTraceModel> fromDb = List.of(
                trace(idCliente, idPedido, t3),
                trace(idCliente, idPedido, t1),
                trace(idCliente, idPedido, t2));
        when(persistencePort.obtenerPorPedido(idPedido)).thenReturn(fromDb);

        List<OrderTraceModel> result = useCase.obtenerTrazabilidadDePedido(idPedido, idCliente);

        verify(persistencePort).obtenerPorPedido(idPedido);
        assertEquals(List.of(t1, t2, t3), result.stream().map(OrderTraceModel::getFechaEvento).toList());
    }

    @Test
    void obtenerTrazabilidadFallaParaSolicitanteAjeno() {
        Long idPedido = 5L;
        List<OrderTraceModel> fromDb = List.of(trace(1L, idPedido, Instant.parse("2024-01-15T10:00:00Z")));
        when(persistencePort.obtenerPorPedido(idPedido)).thenReturn(fromDb);

        UnauthorizedTraceAccessException ex = assertThrows(
                UnauthorizedTraceAccessException.class,
                () -> useCase.obtenerTrazabilidadDePedido(idPedido, 2L));

        assertEquals(DomainErrorMessage.UNAUTHORIZED_TRACE_ACCESS.getMessage(), ex.getMessage());
        verify(persistencePort).obtenerPorPedido(idPedido);
    }

    @Test
    void obtenerTrazabilidadDePedidoSinEventosDevuelveListaVacia() {
        Long idPedido = 10L;
        when(persistencePort.obtenerPorPedido(idPedido)).thenReturn(List.of());

        List<OrderTraceModel> result = useCase.obtenerTrazabilidadDePedido(idPedido, 777L);

        assertTrue(result.isEmpty());
        verify(persistencePort).obtenerPorPedido(idPedido);
    }

    private static OrderTraceModel sampleModel() {
        return trace(1L, 1L, Instant.parse("2024-01-01T00:00:00Z"));
    }

    private static OrderTraceModel trace(Long idCliente, Long idPedido, Instant fecha) {
        return OrderTraceModel.builder()
                .idPedido(idPedido)
                .idCliente(idCliente)
                .idRestaurante(2L)
                .estadoNuevo("PENDIENTE")
                .fechaEvento(fecha)
                .build();
    }
}
