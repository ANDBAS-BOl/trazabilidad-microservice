package com.pragma.powerup.trazabilidad.infrastructure.input.rest;

import com.pragma.powerup.trazabilidad.domain.utils.DomainErrorMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RequestValidationTest {

    private static final String EVENTOS_URL = "/api/v1/trazabilidad/eventos";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postEventos_sinIdPedido_devuelve400ConErrorsIdPedido() throws Exception {
        String body = """
                {"idCliente":1,"idRestaurante":1,"estadoNuevo":"LISTO"}
                """;

        mockMvc.perform(post(EVENTOS_URL)
                        .header("Authorization", "Bearer " + TestJwtTokens.empleado())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DomainErrorMessage.VALIDATION_FAILED.getMessage()))
                .andExpect(jsonPath("$.errors.idPedido").exists());
    }

    @Test
    void postEventos_bodyVacio_devuelve400ConCamposRequeridosEnErrors() throws Exception {
        mockMvc.perform(post(EVENTOS_URL)
                        .header("Authorization", "Bearer " + TestJwtTokens.empleado())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DomainErrorMessage.VALIDATION_FAILED.getMessage()))
                .andExpect(jsonPath("$.errors.idPedido").exists())
                .andExpect(jsonPath("$.errors.idCliente").exists())
                .andExpect(jsonPath("$.errors.idRestaurante").exists())
                .andExpect(jsonPath("$.errors.estadoNuevo").exists());
    }

    @Test
    void postEventos_estadoNuevoBlank_devuelve400() throws Exception {
        String body = """
                {"idPedido":1,"idCliente":1,"idRestaurante":1,"estadoNuevo":"   "}
                """;

        mockMvc.perform(post(EVENTOS_URL)
                        .header("Authorization", "Bearer " + TestJwtTokens.empleado())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(DomainErrorMessage.VALIDATION_FAILED.getMessage()))
                .andExpect(jsonPath("$.errors.estadoNuevo").exists());
    }
}
