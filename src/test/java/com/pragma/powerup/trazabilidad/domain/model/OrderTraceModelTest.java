package com.pragma.powerup.trazabilidad.domain.model;

import com.pragma.powerup.trazabilidad.domain.exception.BusinessRuleException;
import com.pragma.powerup.trazabilidad.domain.utils.DomainErrorMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTraceModelTest {

    @Test
    void constructor_ShouldAssignCurrentTimestamp_WhenDateIsNull() {
        OrderTraceModel model = OrderTraceModel.builder()
                .idPedido(1L).idCliente(1L).idRestaurante(1L)
                .estadoNuevo("PENDIENTE")
                .build();

        assertNotNull(model.getFechaEvento());
        assertTrue(model.getFechaEvento().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    void constructor_WithAllFields_ShouldAutopopulateFechaEvento_WhenNull() {
        Instant before = Instant.now();

        OrderTraceModel model = OrderTraceModel.builder()
                .id("doc-id")
                .idPedido(10L)
                .idCliente(20L)
                .idRestaurante(30L)
                .idEmpleado(40L)
                .estadoAnterior("PENDIENTE")
                .estadoNuevo("EN_PREPARACION")
                .fechaEvento(null)
                .build();

        assertEquals("doc-id", model.getId());
        assertEquals(10L, model.getIdPedido());
        assertEquals(20L, model.getIdCliente());
        assertEquals(30L, model.getIdRestaurante());
        assertEquals(40L, model.getIdEmpleado());
        assertEquals("PENDIENTE", model.getEstadoAnterior());
        assertEquals("EN_PREPARACION", model.getEstadoNuevo());
        assertNotNull(model.getFechaEvento());
        assertFalse(model.getFechaEvento().isBefore(before));
        assertTrue(model.getFechaEvento().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    void constructor_ShouldThrowException_WhenFieldsAreMissing() {
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () ->
                OrderTraceModel.builder().build());
        assertEquals(DomainErrorMessage.REQUIRED_FIELDS.getMessage(), ex.getMessage());
    }

    @Test
    void constructor_ShouldThrow_WhenIdPedidoNull() {
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () ->
                OrderTraceModel.builder()
                        .idCliente(1L)
                        .idRestaurante(1L)
                        .estadoNuevo("PENDIENTE")
                        .build());
        assertEquals(DomainErrorMessage.REQUIRED_FIELDS.getMessage(), ex.getMessage());
    }

    @Test
    void constructor_ShouldThrow_WhenIdClienteNull() {
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () ->
                OrderTraceModel.builder()
                        .idPedido(1L)
                        .idRestaurante(1L)
                        .estadoNuevo("PENDIENTE")
                        .build());
        assertEquals(DomainErrorMessage.REQUIRED_FIELDS.getMessage(), ex.getMessage());
    }

    @Test
    void constructor_ShouldThrow_WhenIdRestauranteNull() {
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () ->
                OrderTraceModel.builder()
                        .idPedido(1L)
                        .idCliente(1L)
                        .estadoNuevo("PENDIENTE")
                        .build());
        assertEquals(DomainErrorMessage.REQUIRED_FIELDS.getMessage(), ex.getMessage());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t"})
    void constructor_ShouldThrow_WhenEstadoNuevoInvalid(String estadoNuevo) {
        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () ->
                OrderTraceModel.builder()
                        .idPedido(1L)
                        .idCliente(1L)
                        .idRestaurante(1L)
                        .estadoNuevo(estadoNuevo)
                        .build());
        assertEquals(DomainErrorMessage.ESTADO_NUEVO_REQUIRED.getMessage(), ex.getMessage());
    }
}
