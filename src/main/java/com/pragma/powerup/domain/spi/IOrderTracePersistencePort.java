package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderTraceModel;

import java.util.List;

public interface IOrderTracePersistencePort {
    void guardarEvento(OrderTraceModel orderTraceModel);

    List<OrderTraceModel> obtenerPorPedido(Long idPedido);
}
