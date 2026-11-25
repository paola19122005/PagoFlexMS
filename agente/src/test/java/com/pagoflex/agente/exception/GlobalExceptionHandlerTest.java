package com.pagoflex.agente.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void validation() {
        BeanPropertyBindingResult binding = new BeanPropertyBindingResult(new Object(), "obj");
        binding.addError(new FieldError("obj", "campo", "invalido"));
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, binding);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setRequestURI("/api/conversaciones");
        assertEquals(HttpStatus.BAD_REQUEST, handler.handleValidation(ex, request).getStatusCode());
    }

    @Test
    void typeMismatch() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "x", String.class, "id", null, new RuntimeException());
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setRequestURI("/api/conversaciones/1");
        assertEquals(HttpStatus.BAD_REQUEST, handler.handleTypeMismatch(ex, request).getStatusCode());
    }

    @Test
    void notReadable() {
        org.springframework.http.converter.HttpMessageNotReadableException ex =
                new org.springframework.http.converter.HttpMessageNotReadableException("bad", null);
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setRequestURI("/api/conversaciones");
        assertEquals(HttpStatus.BAD_REQUEST, handler.handleNotReadable(ex, request).getStatusCode());
    }

    @Test
    void notFound() {
        ConversacionNoEncontradaException ex = new ConversacionNoEncontradaException("nf");
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setRequestURI("/api/conversaciones/99");
        assertEquals(HttpStatus.NOT_FOUND, handler.handleNotFound(ex, request).getStatusCode());
    }

    @Test
    void illegalArgument() {
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setRequestURI("/api/conversaciones");
        assertEquals(HttpStatus.BAD_REQUEST, handler.handleIllegalArgument(new IllegalArgumentException("x"), request).getStatusCode());
    }

    @Test
    void generic() {
        HttpServletRequest request = new MockHttpServletRequest();
        ((MockHttpServletRequest) request).setRequestURI("/api/conversaciones");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, handler.handleGeneric(new RuntimeException("x"), request).getStatusCode());
    }
}
