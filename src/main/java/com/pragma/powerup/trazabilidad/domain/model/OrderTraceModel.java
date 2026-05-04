package com.pragma.powerup.trazabilidad.domain.model;

import com.pragma.powerup.trazabilidad.domain.exception.BusinessRuleException;
import com.pragma.powerup.trazabilidad.domain.utils.DomainErrorMessage;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder(toBuilder = true)
public class OrderTraceModel {
    private final String id;
    private final Long idPedido;
    private final Long idCliente;
    private final Long idRestaurante;
    private final Long idEmpleado;
    private final String estadoAnterior;
    private final String estadoNuevo;
    private final Instant fechaEvento;

    private OrderTraceModel(String id, Long idPedido, Long idCliente, Long idRestaurante,
                            Long idEmpleado, String estadoAnterior, String estadoNuevo, Instant fechaEvento) {
        this.id = id;
        this.idPedido = idPedido;
        this.idCliente = idCliente;
        this.idRestaurante = idRestaurante;
        this.idEmpleado = idEmpleado;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.fechaEvento = fechaEvento;
    }

    public static class OrderTraceModelBuilder {
        public OrderTraceModel build() {
            Instant resolvedFechaEvento = this.fechaEvento == null ? Instant.now() : this.fechaEvento;
            OrderTraceModel model = new OrderTraceModel(
                    id, idPedido, idCliente, idRestaurante, idEmpleado,
                    estadoAnterior, estadoNuevo, resolvedFechaEvento);
            model.assertValid();
            return model;
        }
    }

    private void assertValid() {
        if (idPedido == null || idCliente == null || idRestaurante == null) {
            throw new BusinessRuleException(DomainErrorMessage.REQUIRED_FIELDS.getMessage());
        }
        if (estadoNuevo == null || estadoNuevo.isBlank()) {
            throw new BusinessRuleException(DomainErrorMessage.ESTADO_NUEVO_REQUIRED.getMessage());
        }
    }
}
