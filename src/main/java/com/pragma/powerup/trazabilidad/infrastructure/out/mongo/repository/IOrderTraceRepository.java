package com.pragma.powerup.trazabilidad.infrastructure.out.mongo.repository;

import com.pragma.powerup.trazabilidad.infrastructure.out.mongo.document.OrderTraceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IOrderTraceRepository extends MongoRepository<OrderTraceDocument, String> {
    List<OrderTraceDocument> findByIdPedido(Long idPedido);
}
