package com.pagoflex.reportes.exception;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice 
public class GlobalExceptionHandler {


    @ExceptionHandler(ReporteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleReporteNoEncontrado(
            ReporteNoEncontradoException ex,
            HttpServletRequest request) {

        return build(
                HttpStatus.NOT_FOUND,
                "REPORTE_NO_ENCONTRADO",
                "Not Found",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {

        return build(
                HttpStatus.BAD_REQUEST,
                "DATOS_INVALIDOS",
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<ErrorDetail> details =
                ex.getBindingResult()
                        .getFieldErrors()
                        .stream()
                        .map(error ->
                                new ErrorDetail(
                                        error.getField(),
                                        error.getDefaultMessage()
                                )
                        )
                        .toList();

        return build(
                HttpStatus.BAD_REQUEST,
                "VALIDACION_ERROR",
                "Bad Request",
                "Existen errores de validación",
                request.getRequestURI(),
                details
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(
            Exception ex,
            HttpServletRequest request) {

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "ERROR_INTERNO",
                "Internal Server Error",
                "Error interno del servidor",
                request.getRequestURI(),
                null
        );
    }

    private ResponseEntity<ErrorResponse> build(
            HttpStatus status,
            String code,
            String error,
            String message,
            String path,
            List<ErrorDetail> details) {

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                code,
                error,
                message,
                path,
                details
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }    
}
