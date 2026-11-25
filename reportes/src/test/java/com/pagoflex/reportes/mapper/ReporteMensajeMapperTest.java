package com.pagoflex.reportes.mapper;

import com.pagoflex.reportes.dto.request.ReporteMensajeRequest;
import com.pagoflex.reportes.dto.response.ReporteMensajeResponse;
import com.pagoflex.reportes.entity.ReporteMensaje;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReporteMensajeMapperTest {

    @Test
    void toEntity() {

        ReporteMensajeRequest request =
                new ReporteMensajeRequest();

        request.setIdRemitente(2L);
        request.setRolRemitente("AGENTE");
        request.setContenido("Estamos revisando su caso");
        request.setFechaHora("22-11-2026 20:00");

        ReporteMensajeMapper mapper =
                new ReporteMensajeMapper();

        ReporteMensaje mensaje =
                mapper.toEntity(1L, request);

        assertEquals(1L, mensaje.getIdReporte());
        assertEquals(2L, mensaje.getIdRemitente());
        assertEquals("AGENTE", mensaje.getRolRemitente());
        assertEquals(
                "Estamos revisando su caso",
                mensaje.getContenido()
        );
        assertEquals(
                "22-11-2026 20:00",
                mensaje.getFechaHora()
        );
    }

    @Test
    void toResponse() {

        ReporteMensaje mensaje =
                new ReporteMensaje();

        mensaje.setId(3L);
        mensaje.setIdReporte(1L);
        mensaje.setIdRemitente(2L);
        mensaje.setRolRemitente("USUARIO");
        mensaje.setContenido("Muchas gracias");
        mensaje.setFechaHora("22-11-2026 20:05");

        ReporteMensajeMapper mapper =
                new ReporteMensajeMapper();

        ReporteMensajeResponse response =
                mapper.toResponse(mensaje);

        assertEquals(3L, response.getId());
        assertEquals(1L, response.getIdReporte());
        assertEquals(2L, response.getIdRemitente());
        assertEquals("USUARIO", response.getRolRemitente());
        assertEquals("Muchas gracias", response.getContenido());
        assertEquals(
                "22-11-2026 20:05",
                response.getFechaHora()
        );
    }
}