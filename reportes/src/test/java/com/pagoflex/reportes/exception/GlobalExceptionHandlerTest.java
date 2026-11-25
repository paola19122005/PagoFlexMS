package com.pagoflex.reportes.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.servlet.http.HttpServletRequest;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void validation() {

        BeanPropertyBindingResult binding =
                new BeanPropertyBindingResult(
                        new Object(),
                        "obj"
                );

        binding.addError(
                new FieldError(
                        "obj",
                        "campo",
                        "invalido"
                )
        );

        MethodArgumentNotValidException ex =
                new MethodArgumentNotValidException(
                        null,
                        binding
                );

        HttpServletRequest request =
                new MockHttpServletRequest();

        ((MockHttpServletRequest) request)
                .setRequestURI("/api/reportes");

        assertEquals(
                HttpStatus.BAD_REQUEST,
                handler.handleValidation(
                        ex,
                        request
                ).getStatusCode()
        );
    }

    @Test
    void notFound() {

        ReporteNoEncontradoException ex =
                new ReporteNoEncontradoException(
                        "Reporte no encontrado"
                );

        HttpServletRequest request =
                new MockHttpServletRequest();

        ((MockHttpServletRequest) request)
                .setRequestURI(
                        "/api/reportes/99"
                );

        assertEquals(
                HttpStatus.NOT_FOUND,
                handler.handleReporteNoEncontrado(
                        ex,
                        request
                ).getStatusCode()
        );
    }

    @Test
    void illegalArgument() {

        HttpServletRequest request =
                new MockHttpServletRequest();

        ((MockHttpServletRequest) request)
                .setRequestURI(
                        "/api/reportes/1/estado"
                );

        assertEquals(
                HttpStatus.BAD_REQUEST,
                handler.handleIllegalArgument(
                        new IllegalArgumentException(
                                "Estado inválido"
                        ),
                        request
                ).getStatusCode()
        );
    }

    @Test
    void generic() {

        HttpServletRequest request =
                new MockHttpServletRequest();

        ((MockHttpServletRequest) request)
                .setRequestURI("/api/reportes");

        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                handler.handleGeneric(
                        new RuntimeException("error"),
                        request
                ).getStatusCode()
        );
    }
}
