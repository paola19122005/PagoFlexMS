
package com.pagoflex.pagos.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExcepcionesTest {

    @Test
    void excepcionesDebenConservarSusMensajes() {
        assertEquals("Comprobante no encontrado",
                new ComprobanteNoEncontradoException("Comprobante no encontrado").getMessage());

        assertEquals("Compromiso no encontrado",
                new CompromisoNoEncontradoException("Compromiso no encontrado").getMessage());

        assertEquals("Estado invalido",
                new EstadoCompromisoInvalidoException("Estado invalido").getMessage());

        assertEquals("Folio duplicado",
                new FolioDuplicadoException("Folio duplicado").getMessage());

        assertEquals("Pago duplicado",
                new PagoDuplicadoException("Pago duplicado").getMessage());

        assertEquals("Pago no encontrado",
                new PagoNoEncontradoException("Pago no encontrado").getMessage());

        assertEquals("Usuario inactivo",
                new UsuarioInactivoException("Usuario inactivo").getMessage());

        assertEquals("Usuario no encontrado",
                new UsuarioNoEncontradoException("Usuario no encontrado").getMessage());
    }

    @Test
    void comunicacionServicioDebeConservarMensajeYCausa() {
        RuntimeException causa = new RuntimeException("Error de conexion");

        ComunicacionServicioException exception =
                new ComunicacionServicioException("Error de comunicacion", causa);

        assertEquals("Error de comunicacion", exception.getMessage());
        assertSame(causa, exception.getCause());
    }

    @Test
    void comunicacionServicioDebeAceptarSoloMensaje() {
        ComunicacionServicioException exception =
                new ComunicacionServicioException("Error de comunicacion");

        assertEquals("Error de comunicacion", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void errorDetailDebeConservarSusValores() {
        ErrorDetail detail = new ErrorDetail("rut", "El RUT es obligatorio");

        assertEquals("rut", detail.field());
        assertEquals("El RUT es obligatorio", detail.message());
    }

    @Test
    void errorResponseDebeConservarSusValores() {
        var timestamp = java.time.LocalDateTime.now();
        var details = java.util.List.of(
                new ErrorDetail("rut", "El RUT es obligatorio")
        );

        ErrorResponse response = new ErrorResponse(
                timestamp,
                400,
                "DATOS_INVALIDOS",
                "Bad Request",
                "Datos incorrectos",
                "/api/pagos",
                details
        );

        assertEquals(timestamp, response.timestamp());
        assertEquals(400, response.status());
        assertEquals("DATOS_INVALIDOS", response.code());
        assertEquals("Bad Request", response.error());
        assertEquals("Datos incorrectos", response.message());
        assertEquals("/api/pagos", response.path());
        assertEquals(details, response.details());
    }
}
