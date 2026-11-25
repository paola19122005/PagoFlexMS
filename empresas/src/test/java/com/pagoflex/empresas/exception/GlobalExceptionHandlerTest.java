
package com.pagoflex.empresas.exception;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);

        when(request.getRequestURI()).thenReturn("/api/empresas");
    }

    @Test
    void manejaEmpresaNoEncontrada() {
        EmpresaNoEncontradaException ex =
                new EmpresaNoEncontradaException("Empresa no encontrada");

        ResponseEntity<ErrorResponse> response =
                handler.handleNoEncontrada(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("EMPRESA_NO_ENCONTRADA", response.getBody().getCode());
        assertEquals("Empresa no encontrada", response.getBody().getMessage());
        assertEquals("/api/empresas", response.getBody().getPath());
        assertNull(response.getBody().getDetails());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void manejaEmpresaDuplicada() {
        EmpresaDuplicadaException ex =
                new EmpresaDuplicadaException("La empresa ya existe");

        ResponseEntity<ErrorResponse> response =
                handler.handleDuplicada(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("EMPRESA_DUPLICADA", response.getBody().getCode());
        assertEquals("La empresa ya existe", response.getBody().getMessage());
        assertEquals("/api/empresas", response.getBody().getPath());
        assertNull(response.getBody().getDetails());
    }

    @Test
    void manejaErroresDeValidacion() {
        Object target = new Object();
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(target, "empresa");

        bindingResult.addError(
                new FieldError("empresa", "rut", "El RUT es obligatorio")
        );

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response =
                handler.handleValidation(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("DATOS_INVALIDOS", response.getBody().getCode());
        assertEquals("Existen errores de validación", response.getBody().getMessage());
        assertEquals(1, response.getBody().getDetails().size());
        assertEquals("rut", response.getBody().getDetails().get(0).getField());
        assertEquals("El RUT es obligatorio",
                response.getBody().getDetails().get(0).getMessage());
    }

    @Test
    void manejaErrorGenerico() {
        Exception ex = new Exception("Error de prueba");

        ResponseEntity<ErrorResponse> response =
                handler.handleGeneric(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("ERROR_INTERNO", response.getBody().getCode());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
        assertEquals("/api/empresas", response.getBody().getPath());
        assertNull(response.getBody().getDetails());
    }

    @Test
    void creaYObtieneDatosDeErrorDetail() {
        ErrorDetail detail =
                new ErrorDetail("email", "Formato inválido");

        assertEquals("email", detail.getField());
        assertEquals("Formato inválido", detail.getMessage());
    }

    @Test
    void creaYObtieneDatosDeErrorResponse() {
        LocalDateTime timestamp = LocalDateTime.now();
        List<ErrorDetail> details =
                List.of(new ErrorDetail("rut", "RUT inválido"));

        ErrorResponse response = new ErrorResponse(
                timestamp,
                400,
                "DATOS_INVALIDOS",
                "Bad Request",
                "Existen errores de validación",
                "/api/empresas",
                details
        );

        assertEquals(timestamp, response.getTimestamp());
        assertEquals(400, response.getStatus());
        assertEquals("DATOS_INVALIDOS", response.getCode());
        assertEquals("Bad Request", response.getError());
        assertEquals("Existen errores de validación", response.getMessage());
        assertEquals("/api/empresas", response.getPath());
        assertEquals(details, response.getDetails());
    }

    @Test
    void creaExcepcionEmpresaDuplicada() {
        EmpresaDuplicadaException ex =
                new EmpresaDuplicadaException("Duplicada");

        assertEquals("Duplicada", ex.getMessage());
    }

    @Test
    void creaExcepcionEmpresaNoEncontrada() {
        EmpresaNoEncontradaException ex =
                new EmpresaNoEncontradaException("No encontrada");

        assertEquals("No encontrada", ex.getMessage());
    }
}
