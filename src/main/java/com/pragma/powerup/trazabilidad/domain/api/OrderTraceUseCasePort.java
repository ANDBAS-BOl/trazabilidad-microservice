package com.pragma.powerup.trazabilidad.domain.api;

import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;

import java.util.List;

public interface OrderTraceUseCasePort {
    void registrarEvento(OrderTraceModel orderTraceModel);

    List<OrderTraceModel> obtenerTrazabilidadDePedido(Long idPedido, Long idClienteSolicitante);
}
