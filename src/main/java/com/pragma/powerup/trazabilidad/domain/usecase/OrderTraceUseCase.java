package com.pragma.powerup.trazabilidad.domain.usecase;

import com.pragma.powerup.trazabilidad.domain.api.OrderTraceUseCasePort;
import com.pragma.powerup.trazabilidad.domain.model.OrderTraceHistory;
import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;
import com.pragma.powerup.trazabilidad.domain.spi.OrderTracePersistencePort;

import java.util.List;

public class OrderTraceUseCase implements OrderTraceUseCasePort {

    private final OrderTracePersistencePort orderTracePersistencePort;

    public OrderTraceUseCase(OrderTracePersistencePort orderTracePersistencePort) {
        this.orderTracePersistencePort = orderTracePersistencePort;
    }

    @Override
    public void registrarEvento(OrderTraceModel orderTraceModel) {
        orderTracePersistencePort.guardarEvento(orderTraceModel);
    }

    @Override
    public List<OrderTraceModel> obtenerTrazabilidadDePedido(Long idPedido, Long idClienteSolicitante) {
        List<OrderTraceModel> trazas = orderTracePersistencePort.obtenerPorPedido(idPedido);

        OrderTraceHistory history = new OrderTraceHistory(trazas);

        return history.getSortedTracesForClient(idClienteSolicitante);
    }
}
