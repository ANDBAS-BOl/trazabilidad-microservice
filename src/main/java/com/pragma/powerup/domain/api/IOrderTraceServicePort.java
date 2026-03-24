package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderTraceModel;

import java.util.List;

public interface IOrderTraceServicePort {
    void registrarEvento(OrderTraceModel orderTraceModel);

    List<OrderTraceModel> obtenerTrazabilidadDePedido(Long idPedido, Long idClienteSolicitante);
}
