package com.pragma.powerup.trazabilidad.infrastructure.configuration;

import com.pragma.powerup.trazabilidad.domain.api.OrderTraceUseCasePort;
import com.pragma.powerup.trazabilidad.domain.spi.OrderTracePersistencePort;
import com.pragma.powerup.trazabilidad.domain.usecase.OrderTraceUseCase;
import com.pragma.powerup.trazabilidad.infrastructure.out.mongo.adapter.OrderTraceMongoAdapter;
import com.pragma.powerup.trazabilidad.infrastructure.out.mongo.mapper.IOrderTraceDocumentMapper;
import com.pragma.powerup.trazabilidad.infrastructure.out.mongo.repository.IOrderTraceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IOrderTraceRepository orderTraceRepository;
    private final IOrderTraceDocumentMapper orderTraceDocumentMapper;

    @Bean
    public OrderTracePersistencePort orderTracePersistencePort() {
        return new OrderTraceMongoAdapter(orderTraceRepository, orderTraceDocumentMapper);
    }

    @Bean
    public OrderTraceUseCasePort orderTraceUseCasePort() {
        return new OrderTraceUseCase(orderTracePersistencePort());
    }
}