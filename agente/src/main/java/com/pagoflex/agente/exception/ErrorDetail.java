package com.pagoflex.agente.exception;

public record ErrorDetail(
        String field,
        String message
) {
}
