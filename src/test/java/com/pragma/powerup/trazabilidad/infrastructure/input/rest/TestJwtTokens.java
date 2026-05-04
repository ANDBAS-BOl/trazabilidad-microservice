package com.pragma.powerup.trazabilidad.infrastructure.input.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

final class TestJwtTokens {

    private static final String SECRET = "test_jwt_secret_key_at_least_32_characters_long_for_hmac";

    private TestJwtTokens() {
    }

    static String empleado() {
        return token(3L, "emp@test.com", "EMPLEADO");
    }

    private static String token(Long id, String correo, String rol) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setSubject(id.toString())
                .claim("correo", correo)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3_600_000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
