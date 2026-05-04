package com.pragma.powerup.trazabilidad.domain.spi;

import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;

import java.util.List;

public interface OrderTracePersistencePort {
    void guardarEvento(OrderTraceModel orderTraceModel);

    List<OrderTraceModel> obtenerPorPedido(Long idPedido);
}
