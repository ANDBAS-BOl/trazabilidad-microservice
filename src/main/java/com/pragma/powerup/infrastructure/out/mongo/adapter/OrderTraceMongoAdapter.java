package com.pragma.powerup.infrastructure.out.mongo.adapter;

import com.pragma.powerup.domain.model.OrderTraceModel;
import com.pragma.powerup.domain.spi.IOrderTracePersistencePort;
import com.pragma.powerup.infrastructure.out.mongo.document.OrderTraceDocument;
import com.pragma.powerup.infrastructure.out.mongo.repository.IOrderTraceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderTraceMongoAdapter implements IOrderTracePersistencePort {

    private final IOrderTraceRepository orderTraceRepository;

    @Override
    public void guardarEvento(OrderTraceModel orderTraceModel) {
        orderTraceRepository.save(toDocument(orderTraceModel));
    }

    @Override
    public List<OrderTraceModel> obtenerPorPedido(Long idPedido) {
        return orderTraceRepository.findByIdPedido(idPedido).stream().map(this::toModel).toList();
    }

    private OrderTraceDocument toDocument(OrderTraceModel model) {
        return OrderTraceDocument.builder()
                .id(model.getId())
                .idPedido(model.getIdPedido())
                .idCliente(model.getIdCliente())
                .idRestaurante(model.getIdRestaurante())
                .idEmpleado(model.getIdEmpleado())
                .estadoAnterior(model.getEstadoAnterior())
                .estadoNuevo(model.getEstadoNuevo())
                .fechaEvento(model.getFechaEvento())
                .build();
    }

    private OrderTraceModel toModel(OrderTraceDocument document) {
        return OrderTraceModel.builder()
                .id(document.getId())
                .idPedido(document.getIdPedido())
                .idCliente(document.getIdCliente())
                .idRestaurante(document.getIdRestaurante())
                .idEmpleado(document.getIdEmpleado())
                .estadoAnterior(document.getEstadoAnterior())
                .estadoNuevo(document.getEstadoNuevo())
                .fechaEvento(document.getFechaEvento())
                .build();
    }
}
