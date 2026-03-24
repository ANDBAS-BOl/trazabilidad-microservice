package com.pragma.powerup.infrastructure.out.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "order_trace")
public class OrderTraceDocument {
    @Id
    private String id;
    @Indexed
    private Long idPedido;
    @Indexed
    private Long idCliente;
    private Long idRestaurante;
    private Long idEmpleado;
    private String estadoAnterior;
    private String estadoNuevo;
    private Instant fechaEvento;
}
