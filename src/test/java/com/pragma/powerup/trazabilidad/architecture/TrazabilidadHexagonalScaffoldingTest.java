package com.pragma.powerup.trazabilidad.architecture;

import com.pragma.powerup.trazabilidad.application.handler.IOrderTraceHandler;
import com.pragma.powerup.trazabilidad.domain.api.OrderTraceUseCasePort;
import com.pragma.powerup.trazabilidad.domain.spi.OrderTracePersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TrazabilidadHexagonalScaffoldingTest {

    @Autowired
    private OrderTraceUseCasePort orderTraceUseCasePort;

    @Autowired
    private OrderTracePersistencePort orderTracePersistencePort;

    @Autowired
    private IOrderTraceHandler orderTraceHandler;

    @Test
    void shouldLoadAllHexagonalBeans() {
        assertThat(orderTraceUseCasePort).isNotNull();
        assertThat(orderTracePersistencePort).isNotNull();
        assertThat(orderTraceHandler).isNotNull();
    }
}
