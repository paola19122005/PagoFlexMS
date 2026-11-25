package com.pagoflex.usuarios.exception;

public record ErrorDetail(
        String field,
        String message
) {
}
