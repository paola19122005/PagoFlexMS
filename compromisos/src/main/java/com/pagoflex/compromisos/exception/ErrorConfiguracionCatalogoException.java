package com.pagoflex.compromisos.exception;

public class ErrorConfiguracionCatalogoException extends RuntimeException {

    public ErrorConfiguracionCatalogoException(String message) {
        super(message);
    }

    public ErrorConfiguracionCatalogoException(String message, Throwable cause) {
        super(message, cause);
    }
}