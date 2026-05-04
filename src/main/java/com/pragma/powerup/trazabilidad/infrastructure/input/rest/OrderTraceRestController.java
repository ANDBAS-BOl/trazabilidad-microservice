package com.pragma.powerup.trazabilidad.infrastructure.input.rest;

import com.pragma.powerup.trazabilidad.application.dto.request.CreateOrderTraceRequestDto;
import com.pragma.powerup.trazabilidad.application.dto.response.OrderTraceResponseDto;
import com.pragma.powerup.trazabilidad.application.handler.IOrderTraceHandler;
import com.pragma.powerup.trazabilidad.infrastructure.security.UsuarioPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/trazabilidad")
@RequiredArgsConstructor
@Validated
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
@Tag(name = "Trazabilidad", description = "Registro y consulta de cambios de estado de pedidos")
public class OrderTraceRestController {

    private final IOrderTraceHandler orderTraceHandler;

    @Operation(summary = "Registrar evento de cambio de estado", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAnyRole('EMPLEADO','CLIENTE')")
    @PostMapping("/eventos")
    public ResponseEntity<Void> registrarEvento(@Valid @RequestBody CreateOrderTraceRequestDto requestDto) {
        orderTraceHandler.registrarEvento(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Consultar historial de un pedido propio (HU 17)", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/pedidos/{idPedido}")
    public ResponseEntity<List<OrderTraceResponseDto>> obtenerTrazabilidad(
            @PathVariable Long idPedido,
            @AuthenticationPrincipal UsuarioPrincipal principal) {
        return ResponseEntity.ok(orderTraceHandler.obtenerTrazabilidadDePedido(idPedido, principal.getId()));
    }
}
