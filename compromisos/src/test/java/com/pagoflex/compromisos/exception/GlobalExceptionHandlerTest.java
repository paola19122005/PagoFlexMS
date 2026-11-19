package com.pagoflex.compromisos.exception;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    @Test
    void handleNotFound() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos/1");
        var response = handler.handleNotFound(new CompromisoNoEncontradoException("no"), request);
        assertEquals(404, response.getStatusCode().value());
        assertEquals("COMPROMISO_NO_ENCONTRADO", response.getBody().code());
    }

    @Test
    void handleOrigenConflict() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        var response = handler.handleOrigenDatosInconsistentes(new OrigenDatosInconsistentesException("msg"), request);
        assertEquals(409, response.getStatusCode().value());
        assertEquals("ORIGEN_DATOS_INCONSISTENTES", response.getBody().code());
    }

    @Test
    void handleIllegalArgument() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        var response = handler.handleIllegalArgument(new IllegalArgumentException("msg"), request);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleValidation() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        org.springframework.web.bind.MethodArgumentNotValidException ex =
                new org.springframework.web.bind.MethodArgumentNotValidException(
                        new org.springframework.core.MethodParameter(
                                org.springframework.web.method.HandlerMethod.class.getDeclaredMethods()[0], -1),
                        new org.springframework.validation.BeanPropertyBindingResult(new Object(), "obj"));
        var response = handler.handleValidation(ex, request);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleTypeMismatch() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex =
                new org.springframework.web.method.annotation.MethodArgumentTypeMismatchException(
                        "x", String.class, "id", null, new RuntimeException());
        var response = handler.handleTypeMismatch(ex, request);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleNotReadable() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        var response = handler.handleNotReadable(new org.springframework.http.converter.HttpMessageNotReadableException("msg", (org.springframework.http.HttpInputMessage) null), request);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleEstadoNoEncontrado() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        var response = handler.handleEstadoNoEncontrado(new EstadoNoEncontradoException("msg"), request);
        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    void handleMotivoNoEncontrado() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        var response = handler.handleMotivoNoEncontrado(new MotivoAnulacionNoEncontradoException("msg"), request);
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void handleYaAnulado() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        var response = handler.handleYaAnulado(new CompromisoYaAnuladoException("msg"), request);
        assertEquals(409, response.getStatusCode().value());
    }

    @Test
    void handlePersistencia() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        var response = handler.handlePersistencia(new CompromisoPersistenciaException("msg", new RuntimeException()), request);
        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void handleGeneric() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/compromisos");
        var response = handler.handleGeneric(new RuntimeException("msg"), request);
        assertEquals(500, response.getStatusCode().value());
    }
}
