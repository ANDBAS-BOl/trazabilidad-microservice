package com.pragma.powerup.trazabilidad.domain.model;

import com.pragma.powerup.trazabilidad.domain.exception.UnauthorizedTraceAccessException;
import com.pragma.powerup.trazabilidad.domain.utils.DomainErrorMessage;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTraceHistoryTest {

    private static OrderTraceModel trace(Long idCliente, Instant fecha) {
        return OrderTraceModel.builder()
                .idPedido(1L)
                .idCliente(idCliente)
                .idRestaurante(1L)
                .estadoNuevo("PENDIENTE")
                .fechaEvento(fecha)
                .build();
    }

    @Test
    void getSortedTracesForClient_SortsAscendingByFechaEvento() {
        Instant t1 = Instant.parse("2024-06-01T12:00:00Z");
        Instant t2 = Instant.parse("2024-06-02T12:00:00Z");
        Instant t3 = Instant.parse("2024-06-03T12:00:00Z");

        List<OrderTraceModel> unsorted = List.of(
                trace(1L, t3),
                trace(1L, t1),
                trace(1L, t2));

        OrderTraceHistory history = new OrderTraceHistory(unsorted);
        List<OrderTraceModel> sorted = history.getSortedTracesForClient(1L);

        assertEquals(List.of(t1, t2, t3), sorted.stream().map(OrderTraceModel::getFechaEvento).toList());
    }

    @Test
    void getSortedTracesForClient_EmptyList_DoesNotThrow() {
        OrderTraceHistory history = new OrderTraceHistory(List.of());
        assertDoesNotThrow(() -> history.getSortedTracesForClient(99L));
        assertTrue(history.getSortedTracesForClient(99L).isEmpty());
    }

    @Test
    void getSortedTracesForClient_WhenSolicitanteNotOwner_ThrowsUnauthorized() {
        List<OrderTraceModel> traces = List.of(trace(1L, Instant.now()));

        OrderTraceHistory history = new OrderTraceHistory(traces);
        UnauthorizedTraceAccessException ex = assertThrows(
                UnauthorizedTraceAccessException.class,
                () -> history.getSortedTracesForClient(2L));

        assertEquals(DomainErrorMessage.UNAUTHORIZED_TRACE_ACCESS.getMessage(), ex.getMessage());
    }

    @Test
    void getSortedTracesForClient_WhenSolicitanteIsOwner_ReturnsSortedList() {
        Instant earlier = Instant.parse("2024-01-01T00:00:00Z");
        Instant later = Instant.parse("2024-01-02T00:00:00Z");
        List<OrderTraceModel> traces = List.of(trace(1L, later), trace(1L, earlier));

        OrderTraceHistory history = new OrderTraceHistory(traces);
        List<OrderTraceModel> result = history.getSortedTracesForClient(1L);

        assertEquals(2, result.size());
        assertEquals(earlier, result.get(0).getFechaEvento());
        assertEquals(later, result.get(1).getFechaEvento());
    }
}
