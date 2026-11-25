package com.pagoflex.usuarios.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        List<ErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();
        return build(HttpStatus.BAD_REQUEST, "VALIDACION_FALLIDA", "Bad Request",
                "La solicitud contiene datos invalidos", request.getRequestURI(), details);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                            HttpServletRequest request) {
        String message = "El parametro " + ex.getName() + " tiene un formato invalido";
        return build(HttpStatus.BAD_REQUEST, "PARAMETRO_INVALIDO", "Bad Request", message,
                request.getRequestURI(), null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex,
                                                           HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "JSON_INVALIDO", "Bad Request",
                "El cuerpo de la solicitud no puede interpretarse correctamente",
                request.getRequestURI(), null);
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(UsuarioNoEncontradoException ex,
                                                        HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, "USUARIO_NO_ENCONTRADO", "Not Found",
                ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> handleCredenciales(CredencialesInvalidasException ex,
                                                            HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "CREDENCIALES_INVALIDAS", "Unauthorized",
                ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(CuentaDeshabilitadaException.class)
    public ResponseEntity<ErrorResponse> handleCuentaDeshabilitada(CuentaDeshabilitadaException ex,
                                                                   HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "CUENTA_DESHABILITADA", "Forbidden",
                ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleEmailDuplicado(EmailDuplicadoException ex,
                                                              HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "EMAIL_DUPLICADO", "Conflict",
                ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(RolInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleRolInvalido(RolInvalidoException ex,
                                                           HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "ROL_INVALIDO", "Bad Request",
                ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
                                                               HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "DATOS_INVALIDOS", "Bad Request",
                ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex,
                                                             HttpServletRequest request) {
        return build(HttpStatus.CONFLICT, "CONFLICTO_DATOS", "Conflict",
                "La operacion viola una restriccion de la base de datos", request.getRequestURI(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR_INTERNO", "Internal Server Error",
                "Error interno del servidor", request.getRequestURI(), null);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String code, String error, String message,
                                                String path, List<ErrorDetail> details) {
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
