package com.pagoflex.agente.mapper;

import com.pagoflex.agente.dto.request.MensajeRegistroRequest;
import com.pagoflex.agente.dto.response.MensajeResponse;
import com.pagoflex.agente.entity.Mensaje;
import org.springframework.stereotype.Component;

@Component
public class MensajeMapper {

    public Mensaje toEntity(Long idConversacion, MensajeRegistroRequest request) {
        Mensaje mensaje = new Mensaje();
        mensaje.setIdConversacion(idConversacion);
        mensaje.setIdRemitente(request.getIdRemitente());
        mensaje.setRolRemitente(request.getRolRemitente());
        mensaje.setContenido(request.getContenido());
        mensaje.setFechaHora(request.getFechaHora());
        return mensaje;
    }

    public MensajeResponse toResponse(Mensaje mensaje) {
        MensajeResponse response = new MensajeResponse();
        response.setId(mensaje.getId());
        response.setIdConversacion(mensaje.getIdConversacion());
        response.setIdRemitente(mensaje.getIdRemitente());
        response.setRolRemitente(mensaje.getRolRemitente());
        response.setContenido(mensaje.getContenido());
        response.setFechaHora(mensaje.getFechaHora());
        return response;
    }
}
