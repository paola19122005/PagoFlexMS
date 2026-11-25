package com.pagoflex.agente.mapper;

import com.pagoflex.agente.dto.request.ConversacionRegistroRequest;
import com.pagoflex.agente.dto.response.ConversacionResponse;
import com.pagoflex.agente.entity.Conversacion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConversacionMapperTest {

    @Test
    void toEntityConEstado() {
        ConversacionRegistroRequest request = new ConversacionRegistroRequest();
        request.setIdUsuario(5L);
        request.setFechaCreacion("19-11-2026 20:00");
        request.setEstado("FINALIZADO");
        ConversacionMapper mapper = new ConversacionMapper();
        Conversacion conversacion = mapper.toEntity(request);
        assertEquals(5L, conversacion.getIdUsuario());
        assertEquals("19-11-2026 20:00", conversacion.getFechaCreacion());
        assertEquals("FINALIZADO", conversacion.getEstado());
    }

    @Test
    void toEntitySinEstado() {
        ConversacionRegistroRequest request = new ConversacionRegistroRequest();
        request.setIdUsuario(5L);
        request.setFechaCreacion("19-11-2026 20:00");
        ConversacionMapper mapper = new ConversacionMapper();
        Conversacion conversacion = mapper.toEntity(request);
        assertEquals("PENDIENTE", conversacion.getEstado());
    }

    @Test
    void toEntityEstadoEnBlanco() {
        ConversacionRegistroRequest request = new ConversacionRegistroRequest();
        request.setIdUsuario(5L);
        request.setFechaCreacion("19-11-2026 20:00");
        request.setEstado("   ");
        ConversacionMapper mapper = new ConversacionMapper();
        Conversacion conversacion = mapper.toEntity(request);
        assertEquals("PENDIENTE", conversacion.getEstado());
    }

    @Test
    void toResponse() {
        Conversacion conversacion = new Conversacion();
        conversacion.setId(1L);
        conversacion.setIdUsuario(5L);
        conversacion.setFechaCreacion("19-11-2026 20:00");
        conversacion.setEstado("PENDIENTE");
        ConversacionMapper mapper = new ConversacionMapper();
        ConversacionResponse response = mapper.toResponse(conversacion);
        assertEquals(1L, response.getId());
        assertEquals(5L, response.getIdUsuario());
        assertEquals("19-11-2026 20:00", response.getFechaCreacion());
        assertEquals("PENDIENTE", response.getEstado());
    }
}
