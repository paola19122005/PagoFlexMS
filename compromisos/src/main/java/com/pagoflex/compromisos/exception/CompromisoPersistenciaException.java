package com.pagoflex.compromisos.exception;

public class CompromisoPersistenciaException extends RuntimeException {

    public CompromisoPersistenciaException(String message) {
        super(message);
    }

    public CompromisoPersistenciaException(String message, Throwable cause) {
        super(message, cause);
    }
}
