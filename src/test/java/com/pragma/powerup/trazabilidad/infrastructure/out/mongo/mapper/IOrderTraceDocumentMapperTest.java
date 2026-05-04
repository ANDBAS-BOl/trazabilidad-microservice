package com.pragma.powerup.trazabilidad.infrastructure.out.mongo.mapper;

import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;
import com.pragma.powerup.trazabilidad.infrastructure.out.mongo.document.OrderTraceDocument;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class IOrderTraceDocumentMapperTest {

    private final IOrderTraceDocumentMapper mapper = new IOrderTraceDocumentMapperImpl();

    @Test
    void toDocument_mapsModelToMongoDocument() {
        Instant fecha = Instant.parse("2024-06-01T10:00:00Z");
        OrderTraceModel model = OrderTraceModel.builder()
                .id("abc")
                .idPedido(100L)
                .idCliente(200L)
                .idRestaurante(300L)
                .idEmpleado(400L)
                .estadoAnterior("PENDIENTE")
                .estadoNuevo("EN_PREPARACION")
                .fechaEvento(fecha)
                .build();

        OrderTraceDocument doc = mapper.toDocument(model);

        assertEquals("abc", doc.getId());
        assertEquals(100L, doc.getIdPedido());
        assertEquals(200L, doc.getIdCliente());
        assertEquals(300L, doc.getIdRestaurante());
        assertEquals(400L, doc.getIdEmpleado());
        assertEquals("PENDIENTE", doc.getEstadoAnterior());
        assertEquals("EN_PREPARACION", doc.getEstadoNuevo());
        assertEquals(fecha, doc.getFechaEvento());
    }

    @Test
    void toModel_mapsDocumentToImmutableDomainCopy() {
        Instant fecha = Instant.parse("2024-06-02T11:30:00Z");
        OrderTraceDocument doc = OrderTraceDocument.builder()
                .id("doc-1")
                .idPedido(1L)
                .idCliente(2L)
                .idRestaurante(3L)
                .idEmpleado(4L)
                .estadoAnterior("X")
                .estadoNuevo("Y")
                .fechaEvento(fecha)
                .build();

        OrderTraceModel model = mapper.toModel(doc);

        assertNotSame(doc, model);
        assertEquals("doc-1", model.getId());
        assertEquals(1L, model.getIdPedido());
        assertEquals(2L, model.getIdCliente());
        assertEquals(3L, model.getIdRestaurante());
        assertEquals(4L, model.getIdEmpleado());
        assertEquals("X", model.getEstadoAnterior());
        assertEquals("Y", model.getEstadoNuevo());
        assertEquals(fecha, model.getFechaEvento());

        doc.setIdPedido(999L);
        assertEquals(1L, model.getIdPedido());
    }
}
