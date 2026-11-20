package com.pagoflex.agente.mapper;

import com.pagoflex.agente.dto.request.MensajeRegistroRequest;
import com.pagoflex.agente.dto.response.MensajeResponse;
import com.pagoflex.agente.entity.Mensaje;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MensajeMapperTest {

    @Test
    void toEntity() {
        MensajeRegistroRequest request = new MensajeRegistroRequest();
        request.setIdRemitente(2L);
        request.setRolRemitente("AGENTE");
        request.setContenido("Hola");
        request.setFechaHora("19-11-2026 20:05");
        MensajeMapper mapper = new MensajeMapper();
        Mensaje mensaje = mapper.toEntity(1L, request);
        assertEquals(1L, mensaje.getIdConversacion());
        assertEquals(2L, mensaje.getIdRemitente());
        assertEquals("AGENTE", mensaje.getRolRemitente());
        assertEquals("Hola", mensaje.getContenido());
        assertEquals("19-11-2026 20:05", mensaje.getFechaHora());
    }

    @Test
    void toResponse() {
        Mensaje mensaje = new Mensaje();
        mensaje.setId(3L);
        mensaje.setIdConversacion(1L);
        mensaje.setIdRemitente(2L);
        mensaje.setRolRemitente("USUARIO");
        mensaje.setContenido("Hola");
        mensaje.setFechaHora("19-11-2026 20:05");
        MensajeMapper mapper = new MensajeMapper();
        MensajeResponse response = mapper.toResponse(mensaje);
        assertEquals(3L, response.getId());
        assertEquals(1L, response.getIdConversacion());
        assertEquals(2L, response.getIdRemitente());
        assertEquals("USUARIO", response.getRolRemitente());
        assertEquals("Hola", response.getContenido());
        assertEquals("19-11-2026 20:05", response.getFechaHora());
    }
}
