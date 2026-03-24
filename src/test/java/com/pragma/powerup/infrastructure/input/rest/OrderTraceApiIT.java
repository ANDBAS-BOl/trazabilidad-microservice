package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.infrastructure.out.mongo.document.OrderTraceDocument;
import com.pragma.powerup.infrastructure.out.mongo.repository.IOrderTraceRepository;
import com.pragma.powerup.infrastructure.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderTraceApiIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IOrderTraceRepository orderTraceRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        orderTraceRepository.deleteAll();
    }

    @Test
    void registrarEventoYConsultarPropioPedido_ok() throws Exception {
        String tokenEmpleado = jwtTokenProvider.generateToken(200L, "emp@test.local", "EMPLEADO");
        String tokenCliente = jwtTokenProvider.generateToken(100L, "cli@test.local", "CLIENTE");

        String body = """
                {
                  "idPedido": 9001,
                  "idCliente": 100,
                  "idRestaurante": 10,
                  "idEmpleado": 200,
                  "estadoAnterior": "PENDIENTE",
                  "estadoNuevo": "EN_PREPARACION"
                }
                """;

        mockMvc.perform(post("/api/v1/trazabilidad/eventos")
                        .header("Authorization", "Bearer " + tokenEmpleado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/trazabilidad/pedidos/9001")
                        .header("Authorization", "Bearer " + tokenCliente))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPedido").value(9001))
                .andExpect(jsonPath("$[0].estadoNuevo").value("EN_PREPARACION"));
    }

    @Test
    void clienteNoPuedeConsultarPedidoDeOtro_forbidden() throws Exception {
        orderTraceRepository.saveAll(List.of(
                OrderTraceDocument.builder()
                        .idPedido(8001L)
                        .idCliente(999L)
                        .idRestaurante(11L)
                        .estadoAnterior("PENDIENTE")
                        .estadoNuevo("EN_PREPARACION")
                        .fechaEvento(Instant.now())
                        .build()
        ));
        String tokenCliente = jwtTokenProvider.generateToken(100L, "cli@test.local", "CLIENTE");

        mockMvc.perform(get("/api/v1/trazabilidad/pedidos/8001")
                        .header("Authorization", "Bearer " + tokenCliente))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void endpointProtegidoSinJwt_unauthorized() throws Exception {
        mockMvc.perform(get("/api/v1/trazabilidad/pedidos/1"))
                .andExpect(status().isForbidden());
    }
}
