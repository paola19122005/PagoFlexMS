
package com.pagoflex.pagos.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/pagos");
    }

    private void verificarRespuesta(
            ResponseEntity<ErrorResponse> response,
            HttpStatus status,
            String code,
            String message) {

        assertEquals(status, response.getStatusCode());

        ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.timestamp());
        assertEquals(status.value(), body.status());
        assertEquals(code, body.code());
        assertEquals(message, body.message());
        assertEquals("/api/pagos", body.path());
    }

    @Test
    void handleIllegalArgument() {
        var response = handler.handleIllegalArgument(
                new IllegalArgumentException("Datos invalidos"), request);

        verificarRespuesta(response, HttpStatus.BAD_REQUEST,
                "DATOS_INVALIDOS", "Datos invalidos");
        assertNull(response.getBody().details());
    }

    @Test
    void handlePagoNoEncontrado() {
        var response = handler.handlePagoNoEncontrado(
                new PagoNoEncontradoException("Pago no encontrado"), request);

        verificarRespuesta(response, HttpStatus.NOT_FOUND,
                "PAGO_NO_ENCONTRADO", "Pago no encontrado");
    }

    @Test
    void handleComprobanteNoEncontrado() {
        var response = handler.handleComprobanteNoEncontrado(
                new ComprobanteNoEncontradoException("Comprobante no encontrado"), request);

        verificarRespuesta(response, HttpStatus.NOT_FOUND,
                "COMPROBANTE_NO_ENCONTRADO", "Comprobante no encontrado");
    }

    @Test
    void handleCompromisoNoEncontrado() {
        var response = handler.handleCompromisoNoEncontrado(
                new CompromisoNoEncontradoException("Compromiso no encontrado"), request);

        verificarRespuesta(response, HttpStatus.NOT_FOUND,
                "COMPROMISO_NO_ENCONTRADO", "Compromiso no encontrado");
    }

    @Test
    void handleUsuarioNoEncontrado() {
        var response = handler.handleUsuarioNoEncontrado(
                new UsuarioNoEncontradoException("Usuario no encontrado"), request);

        verificarRespuesta(response, HttpStatus.NOT_FOUND,
                "USUARIO_NO_ENCONTRADO", "Usuario no encontrado");
    }

    @Test
    void handlePagoDuplicado() {
        var response = handler.handlePagoDuplicado(
                new PagoDuplicadoException("Pago duplicado"), request);

        verificarRespuesta(response, HttpStatus.CONFLICT,
                "PAGO_DUPLICADO", "Pago duplicado");
    }

    @Test
    void handleFolioDuplicado() {
        var response = handler.handleFolioDuplicado(
                new FolioDuplicadoException("Folio duplicado"), request);

        verificarRespuesta(response, HttpStatus.CONFLICT,
                "FOLIO_DUPLICADO", "Folio duplicado");
    }

    @Test
    void handleUsuarioInactivo() {
        var response = handler.handleUsuarioInactivo(
                new UsuarioInactivoException("Usuario inactivo"), request);

        verificarRespuesta(response, HttpStatus.CONFLICT,
                "USUARIO_INACTIVO", "Usuario inactivo");
    }

    @Test
    void handleEstadoCompromisoInvalido() {
        var response = handler.handleEstadoCompromisoInvalido(
                new EstadoCompromisoInvalidoException("Estado invalido"), request);

        verificarRespuesta(response, HttpStatus.CONFLICT,
                "ESTADO_COMPROMISO_INVALIDO", "Estado invalido");
    }

    @Test
    void handleComunicacionServicio() {
        var response = handler.handleComunicacionServicio(
                new ComunicacionServicioException("Error de comunicacion"), request);

        verificarRespuesta(response, HttpStatus.BAD_GATEWAY,
                "ERROR_COMUNICACION_SERVICIO", "Error de comunicacion");
    }

    @Test
    void handleGeneric() {
        var response = handler.handleGeneric(
                new RuntimeException("Error interno"), request);

        verificarRespuesta(response, HttpStatus.INTERNAL_SERVER_ERROR,
                "ERROR_INTERNO", "Error interno del servidor");
    }

    @Test
    void handleNotReadable() {
        var inputMessage = mock(
                org.springframework.http.HttpInputMessage.class
        );

        var exception = new HttpMessageNotReadableException(
                "JSON incorrecto",
                inputMessage
        );

        var response = handler.handleNotReadable(exception, request);

        verificarRespuesta(response, HttpStatus.BAD_REQUEST,
                "JSON_INVALIDO",
                "El cuerpo de la solicitud no puede interpretarse correctamente");
    }

    @Test
    void handleTypeMismatch() {
        var exception = new MethodArgumentTypeMismatchException(
                "abc", Long.class, "id", null, new IllegalArgumentException());

        var response = handler.handleTypeMismatch(exception, request);

        verificarRespuesta(response, HttpStatus.BAD_REQUEST,
                "PARAMETRO_INVALIDO",
                "El parametro id tiene un formato invalido");
    }

    @Test
    void handleValidation() {
        var target = new Object();
        var bindingResult = new BeanPropertyBindingResult(target, "request");
        bindingResult.addError(new FieldError(
                "request", "rut", "El RUT es obligatorio"));

        var exception = new MethodArgumentNotValidException(null, bindingResult);

        var response = handler.handleValidation(exception, request);

        verificarRespuesta(response, HttpStatus.BAD_REQUEST,
                "VALIDACION_FALLIDA",
                "La solicitud contiene datos invalidos");

        assertNotNull(response.getBody().details());
        assertEquals(1, response.getBody().details().size());
        assertEquals("rut", response.getBody().details().get(0).field());
        assertEquals("El RUT es obligatorio",
                response.getBody().details().get(0).message());
    }
}
