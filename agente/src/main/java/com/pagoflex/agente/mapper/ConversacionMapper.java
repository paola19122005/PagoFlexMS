package com.pagoflex.agente.mapper;

import com.pagoflex.agente.dto.request.ConversacionRegistroRequest;
import com.pagoflex.agente.dto.response.ConversacionResponse;
import com.pagoflex.agente.entity.Conversacion;
import org.springframework.stereotype.Component;

@Component
public class ConversacionMapper {

    public Conversacion toEntity(ConversacionRegistroRequest request) {
        Conversacion conversacion = new Conversacion();
        conversacion.setIdUsuario(request.getIdUsuario());
        conversacion.setFechaCreacion(request.getFechaCreacion());
        if (request.getEstado() != null && !request.getEstado().isBlank()) {
            conversacion.setEstado(request.getEstado());
        }
        return conversacion;
    }

    public ConversacionResponse toResponse(Conversacion conversacion) {
        ConversacionResponse response = new ConversacionResponse();
        response.setId(conversacion.getId());
        response.setIdUsuario(conversacion.getIdUsuario());
        response.setFechaCreacion(conversacion.getFechaCreacion());
        response.setEstado(conversacion.getEstado());
        return response;
    }
}
