package com.pagoflex.compromisos.exception;

public record ErrorDetail(
        String field,
        String message
) {
}
