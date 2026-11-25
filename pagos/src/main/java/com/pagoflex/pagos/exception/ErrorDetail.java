package com.pagoflex.pagos.exception;

public record ErrorDetail(
        String field,
        String message
) {
}
