package com.pagoflex.pagos.exception;

public class ComunicacionServicioException extends RuntimeException {

    public ComunicacionServicioException(String message) {
        super(message);
    }

    public ComunicacionServicioException(String message, Throwable cause) {
        super(message, cause);
    }
}
