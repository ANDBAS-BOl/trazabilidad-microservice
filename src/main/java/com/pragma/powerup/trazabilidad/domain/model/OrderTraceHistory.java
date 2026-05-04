package com.pragma.powerup.trazabilidad.domain.model;

import com.pragma.powerup.trazabilidad.domain.exception.UnauthorizedTraceAccessException;
import com.pragma.powerup.trazabilidad.domain.utils.DomainErrorMessage;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;

@Getter
public class OrderTraceHistory {

    private final List<OrderTraceModel> traces;

    public OrderTraceHistory(List<OrderTraceModel> traces) {
        this.traces = (traces != null) ? traces : List.of();
    }

    public List<OrderTraceModel> getSortedTracesForClient(Long idClienteSolicitante) {
        validateOwnership(idClienteSolicitante);

        return traces.stream()
                .sorted(Comparator.comparing(OrderTraceModel::getFechaEvento))
                .toList();
    }

    private void validateOwnership(Long idClienteSolicitante) {
        if (traces.isEmpty()) {
            return;
        }

        boolean belongsToClient = traces.stream()
                .allMatch(trace -> trace.getIdCliente().equals(idClienteSolicitante));

        if (!belongsToClient) {
            throw new UnauthorizedTraceAccessException(DomainErrorMessage.UNAUTHORIZED_TRACE_ACCESS.getMessage());
        }
    }
}
