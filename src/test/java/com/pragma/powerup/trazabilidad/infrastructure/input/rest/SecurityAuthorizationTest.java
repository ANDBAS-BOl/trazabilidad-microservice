package com.pragma.powerup.trazabilidad.infrastructure.input.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityAuthorizationTest {

    private static final String SECRET = "test_jwt_secret_key_at_least_32_characters_long_for_hmac";
    private static final String BASE = "/api/v1/trazabilidad";

    private static final String EVENTO_BODY = """
            {"idPedido":1,"idCliente":1,"idRestaurante":1,"estadoNuevo":"LISTO"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class NoTokenTests {

        @Test
        void postEventosSinToken() throws Exception {
            mockMvc.perform(post(BASE + "/eventos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(EVENTO_BODY))
                    .andExpect(status().isForbidden());
        }

        @Test
        void getPedidoSinToken() throws Exception {
            mockMvc.perform(get(BASE + "/pedidos/1"))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class PostEventosSecurity {

        private static final String URL = BASE + "/eventos";

        @Test
        void empleadoPermitido() throws Exception {
            assertNotSecurityForbidden(post(URL).header("Authorization", bearer(token("EMPLEADO")))
                    .contentType(MediaType.APPLICATION_JSON).content(EVENTO_BODY));
        }

        @Test
        void clientePermitido() throws Exception {
            assertNotSecurityForbidden(post(URL).header("Authorization", bearer(token("CLIENTE")))
                    .contentType(MediaType.APPLICATION_JSON).content(EVENTO_BODY));
        }

        @ParameterizedTest
        @ValueSource(strings = {"ADMINISTRADOR", "PROPIETARIO"})
        void otrosRolesProhibidos(String rol) throws Exception {
            mockMvc.perform(post(URL).header("Authorization", bearer(token(rol)))
                            .contentType(MediaType.APPLICATION_JSON).content(EVENTO_BODY))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class GetPedidoSecurity {

        private static final String URL = BASE + "/pedidos/99";

        @Test
        void clientePermitido() throws Exception {
            assertNotSecurityForbidden(get(URL).header("Authorization", bearer(token("CLIENTE"))));
        }

        @ParameterizedTest
        @ValueSource(strings = {"ADMINISTRADOR", "PROPIETARIO", "EMPLEADO"})
        void otrosRolesProhibidos(String rol) throws Exception {
            mockMvc.perform(get(URL).header("Authorization", bearer(token(rol))))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class InvalidTokenTests {

        @Test
        void tokenExpiradoRechazado() throws Exception {
            String expired = expiredToken("EMPLEADO");
            mockMvc.perform(post(BASE + "/eventos")
                            .header("Authorization", bearer(expired))
                            .contentType(MediaType.APPLICATION_JSON).content(EVENTO_BODY))
                    .andExpect(status().isForbidden());
        }

        @Test
        void tokenMalformadoRechazado() throws Exception {
            mockMvc.perform(post(BASE + "/eventos")
                            .header("Authorization", "Bearer not.a.valid.jwt")
                            .contentType(MediaType.APPLICATION_JSON).content(EVENTO_BODY))
                    .andExpect(status().isForbidden());
        }

        @Test
        void tokenConClaveDistintaRechazado() throws Exception {
            SecretKey wrongKey = Keys.hmacShaKeyFor(
                    "another_secret_key_that_is_at_least_32_chars!".getBytes(StandardCharsets.UTF_8));
            String wrongToken = Jwts.builder()
                    .setSubject("1")
                    .claim("correo", "a@test.com")
                    .claim("rol", "EMPLEADO")
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                    .signWith(wrongKey, SignatureAlgorithm.HS256)
                    .compact();
            mockMvc.perform(post(BASE + "/eventos")
                            .header("Authorization", bearer(wrongToken))
                            .contentType(MediaType.APPLICATION_JSON).content(EVENTO_BODY))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    class PublicEndpointsTests {

        @Test
        void swaggerUiEsPublico() throws Exception {
            mockMvc.perform(get("/swagger-ui/index.html"))
                    .andExpect(result -> {
                        int s = result.getResponse().getStatus();
                        org.junit.jupiter.api.Assertions.assertTrue(
                                s == 200 || s == 302,
                                "Swagger deberia ser publico, pero recibio status " + s);
                    });
        }

        @Test
        void apiDocsEsPublico() throws Exception {
            mockMvc.perform(get("/v3/api-docs"))
                    .andExpect(status().isOk());
        }
    }

    private void assertNotSecurityForbidden(org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder req)
            throws Exception {
        ResultActions result = mockMvc.perform(req);
        int httpStatus = result.andReturn().getResponse().getStatus();
        if (httpStatus == 403) {
            String body = result.andReturn().getResponse().getContentAsString();
            org.junit.jupiter.api.Assertions.assertTrue(
                    body.contains("\"message\""),
                    "El endpoint rechazó con 403 por seguridad (no por lógica de negocio). Body: " + body);
        }
    }

    private static String bearer(String token) {
        return "Bearer " + token;
    }

    private static String token(String rol) {
        long id = switch (rol) {
            case "ADMINISTRADOR" -> 1L;
            case "PROPIETARIO" -> 2L;
            case "EMPLEADO" -> 3L;
            case "CLIENTE" -> 4L;
            default -> throw new IllegalArgumentException("Rol: " + rol);
        };
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(Long.toString(id))
                .claim("correo", id + "@test.com")
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private static String expiredToken(String rol) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject("3")
                .claim("correo", "x@test.com")
                .claim("rol", rol)
                .setIssuedAt(new Date(System.currentTimeMillis() - 7_200_000))
                .setExpiration(new Date(System.currentTimeMillis() - 3_600_000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
