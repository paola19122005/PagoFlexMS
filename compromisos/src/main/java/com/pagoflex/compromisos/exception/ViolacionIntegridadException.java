package com.pagoflex.compromisos.exception;

public class ViolacionIntegridadException extends RuntimeException {

    private final String code;

    public ViolacionIntegridadException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}