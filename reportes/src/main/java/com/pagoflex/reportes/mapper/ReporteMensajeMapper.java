package com.pagoflex.reportes.mapper;

import org.springframework.stereotype.Component;

import com.pagoflex.reportes.dto.request.ReporteMensajeRequest;
import com.pagoflex.reportes.dto.response.ReporteMensajeResponse;
import com.pagoflex.reportes.entity.ReporteMensaje;

@Component
public class ReporteMensajeMapper {

    public ReporteMensaje toEntity(
            Long idReporte,
            ReporteMensajeRequest request) {

        ReporteMensaje mensaje = new ReporteMensaje();

        mensaje.setIdReporte(idReporte);
        mensaje.setIdRemitente(request.getIdRemitente());
        mensaje.setRolRemitente(request.getRolRemitente());
        mensaje.setContenido(request.getContenido());
        mensaje.setFechaHora(request.getFechaHora());

        return mensaje;
    }

    public ReporteMensajeResponse toResponse(ReporteMensaje mensaje) {

        ReporteMensajeResponse response =
                new ReporteMensajeResponse();

        response.setId(mensaje.getId());
        response.setIdReporte(mensaje.getIdReporte());
        response.setIdRemitente(mensaje.getIdRemitente());
        response.setRolRemitente(mensaje.getRolRemitente());
        response.setContenido(mensaje.getContenido());
        response.setFechaHora(mensaje.getFechaHora());

        return response;
    }
}
