package com.pagoflex.compromisos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                              HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "DATOS_INVALIDOS", "Bad Request", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                         HttpServletRequest request) {
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        return build(HttpStatus.BAD_REQUEST, "VALIDACION_FALLIDA", "Bad Request", "La solicitud contiene datos invalidos", request.getRequestURI(), details);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                           HttpServletRequest request) {
        String message = "El parametro " + ex.getName() + " tiene un formato invalido";
        return build(HttpStatus.BAD_REQUEST, "PARAMETRO_INVALIDO", "Bad Request", message, request.getRequestURI(), null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex,
                                                          HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "JSON_INVALIDO", "Bad Request", "El cuerpo de la solicitud no puede interpretarse correctamente", request.getRequestURI(), null);
    }

    @ExceptionHandler(CompromisoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(CompromisoNoEncontradoException ex,
                                                       HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "COMPROMISO_NO_ENCONTRADO", "Not Found", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(EstadoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleEstadoNoEncontrado(EstadoNoEncontradoException ex,
                                                                 HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "ESTADO_NO_ENCONTRADO", "Bad Request", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(MotivoAnulacionNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleMotivoNoEncontrado(MotivoAnulacionNoEncontradoException ex,
                                                                 HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_CONFIGURACION_CATALOGO", "Internal Server Error", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(CompromisoYaAnuladoException.class)
    public ResponseEntity<ErrorResponse> handleYaAnulado(CompromisoYaAnuladoException ex,
                                                        HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "COMPROMISO_YA_ANULADO", "Conflict", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(OrigenDatosInconsistentesException.class)
    public ResponseEntity<ErrorResponse> handleOrigenDatosInconsistentes(OrigenDatosInconsistentesException ex,
                                                                          HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "ORIGEN_DATOS_INCONSISTENTES", "Conflict", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ViolacionIntegridadException.class)
    public ResponseEntity<ErrorResponse> handleViolacionIntegridad(ViolacionIntegridadException ex,
                                                                  HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, ex.getCode(), "Conflict", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ErrorConfiguracionCatalogoException.class)
    public ResponseEntity<ErrorResponse> handleErrorConfiguracion(ErrorConfiguracionCatalogoException ex,
                                                                 HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_CONFIGURACION_CATALOGO", "Internal Server Error", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(CompromisoPersistenciaException.class)
    public ResponseEntity<ErrorResponse> handlePersistencia(CompromisoPersistenciaException ex,
                                                           HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_PERSISTENCIA", "Internal Server Error", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO", "Internal Server Error", "Error interno del servidor", request.getRequestURI(), null);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String code, String error, String message, String path, List<ErrorDetail> details) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                code,
                error,
                message,
                path,
                details
        );
        return ResponseEntity.status(status).body(body);
    }
}