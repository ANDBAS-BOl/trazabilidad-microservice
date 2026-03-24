package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IOrderTraceServicePort;
import com.pragma.powerup.domain.spi.IOrderTracePersistencePort;
import com.pragma.powerup.domain.usecase.OrderTraceUseCase;
import com.pragma.powerup.infrastructure.out.mongo.adapter.OrderTraceMongoAdapter;
import com.pragma.powerup.infrastructure.out.mongo.repository.IOrderTraceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final IOrderTraceRepository orderTraceRepository;

    @Bean
    public IOrderTracePersistencePort orderTracePersistencePort() {
        return new OrderTraceMongoAdapter(orderTraceRepository);
    }

    @Bean
    public IOrderTraceServicePort orderTraceServicePort() {
        return new OrderTraceUseCase(orderTracePersistencePort());
    }
}