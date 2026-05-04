package com.pragma.powerup.trazabilidad.infrastructure.out.mongo.mapper;

import com.pragma.powerup.trazabilidad.domain.model.OrderTraceModel;
import com.pragma.powerup.trazabilidad.infrastructure.out.mongo.document.OrderTraceDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IOrderTraceDocumentMapper {

    OrderTraceDocument toDocument(OrderTraceModel model);

    OrderTraceModel toModel(OrderTraceDocument document);
}
