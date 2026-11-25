package com.pagoflex.pagos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

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

    @ExceptionHandler(PagoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handlePagoNoEncontrado(PagoNoEncontradoException ex,
                                                                HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "PAGO_NO_ENCONTRADO", "Not Found", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ComprobanteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleComprobanteNoEncontrado(ComprobanteNoEncontradoException ex,
                                                                       HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "COMPROBANTE_NO_ENCONTRADO", "Not Found", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(CompromisoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleCompromisoNoEncontrado(CompromisoNoEncontradoException ex,
                                                                      HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "COMPROMISO_NO_ENCONTRADO", "Not Found", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioNoEncontrado(UsuarioNoEncontradoException ex,
                                                                   HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "USUARIO_NO_ENCONTRADO", "Not Found", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(PagoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handlePagoDuplicado(PagoDuplicadoException ex,
                                                             HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "PAGO_DUPLICADO", "Conflict", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(FolioDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleFolioDuplicado(FolioDuplicadoException ex,
                                                              HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "FOLIO_DUPLICADO", "Conflict", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(UsuarioInactivoException.class)
    public ResponseEntity<ErrorResponse> handleUsuarioInactivo(UsuarioInactivoException ex,
                                                               HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "USUARIO_INACTIVO", "Conflict", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(EstadoCompromisoInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleEstadoCompromisoInvalido(EstadoCompromisoInvalidoException ex,
                                                                        HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "ESTADO_COMPROMISO_INVALIDO", "Conflict", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(ComunicacionServicioException.class)
    public ResponseEntity<ErrorResponse> handleComunicacionServicio(ComunicacionServicioException ex,
                                                                   HttpServletRequest request) {
        return build(HttpStatus.BAD_GATEWAY, "ERROR_COMUNICACION_SERVICIO", "Bad Gateway", ex.getMessage(), request.getRequestURI(), null);
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
