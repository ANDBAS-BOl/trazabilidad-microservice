package com.pragma.powerup.trazabilidad.infrastructure.out.mongo.adapter;

import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;
import com.pragma.powerup.trazabilidad.domain.spi.OrderTracePersistencePort;
import com.pragma.powerup.trazabilidad.infrastructure.out.mongo.mapper.IOrderTraceDocumentMapper;
import com.pragma.powerup.trazabilidad.infrastructure.out.mongo.repository.IOrderTraceRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderTraceMongoAdapter implements OrderTracePersistencePort {

    private final IOrderTraceRepository orderTraceRepository;
    private final IOrderTraceDocumentMapper orderTraceDocumentMapper;

    @Override
    public void guardarEvento(OrderTraceModel orderTraceModel) {
        orderTraceRepository.save(orderTraceDocumentMapper.toDocument(orderTraceModel));
    }

    @Override
    public List<OrderTraceModel> obtenerPorPedido(Long idPedido) {
        return orderTraceRepository.findByIdPedido(idPedido).stream()
                .map(orderTraceDocumentMapper::toModel)
                .toList();
    }
}
