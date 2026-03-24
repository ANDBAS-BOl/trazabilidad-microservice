package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderTraceServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.exception.UnauthorizedTraceAccessException;
import com.pragma.powerup.domain.model.OrderTraceModel;
import com.pragma.powerup.domain.spi.IOrderTracePersistencePort;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public class OrderTraceUseCase implements IOrderTraceServicePort {

    private static final String ACCESS_DENIED_MESSAGE = "No puedes consultar trazabilidad de pedidos de otro cliente";

    private final IOrderTracePersistencePort orderTracePersistencePort;

    public OrderTraceUseCase(IOrderTracePersistencePort orderTracePersistencePort) {
        this.orderTracePersistencePort = orderTracePersistencePort;
    }

    @Override
    public void registrarEvento(OrderTraceModel orderTraceModel) {
        if (orderTraceModel.getIdPedido() == null || orderTraceModel.getIdCliente() == null || orderTraceModel.getIdRestaurante() == null) {
            throw new DomainException("idPedido, idCliente e idRestaurante son obligatorios");
        }
        if (orderTraceModel.getEstadoNuevo() == null || orderTraceModel.getEstadoNuevo().isBlank()) {
            throw new DomainException("estadoNuevo es obligatorio");
        }
        if (orderTraceModel.getFechaEvento() == null) {
            orderTraceModel.setFechaEvento(Instant.now());
        }
        orderTracePersistencePort.guardarEvento(orderTraceModel);
    }

    @Override
    public List<OrderTraceModel> obtenerTrazabilidadDePedido(Long idPedido, Long idClienteSolicitante) {
        List<OrderTraceModel> trazas = orderTracePersistencePort.obtenerPorPedido(idPedido);
        boolean perteneceAlCliente = trazas.stream().allMatch(item -> item.getIdCliente().equals(idClienteSolicitante));
        if (!trazas.isEmpty() && !perteneceAlCliente) {
            throw new UnauthorizedTraceAccessException(ACCESS_DENIED_MESSAGE);
        }
        return trazas.stream()
                .sorted(Comparator.comparing(OrderTraceModel::getFechaEvento))
                .toList();
    }
}
