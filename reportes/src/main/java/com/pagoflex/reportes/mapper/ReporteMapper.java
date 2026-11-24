package com.pagoflex.reportes.mapper;

import org.springframework.stereotype.Component;

import com.pagoflex.reportes.dto.request.ReporteRegistroRequest;
import com.pagoflex.reportes.dto.response.ReporteResponse;
import com.pagoflex.reportes.entity.Reporte;

@Component
public class ReporteMapper {

    public Reporte toEntity(ReporteRegistroRequest request) {

        Reporte reporte = new Reporte();

        reporte.setTipo(request.getTipo());
        reporte.setCompromiso(request.getCompromiso());
        reporte.setDescripcion(request.getDescripcion());
        reporte.setFecha(request.getFecha());
        reporte.setIdUsuario(request.getIdUsuario());
        reporte.setEstado("PENDIENTE");

        return reporte;
    }

    public ReporteResponse toResponse(Reporte reporte) {

        ReporteResponse response = new ReporteResponse();

        response.setId(reporte.getId());
        response.setTipo(reporte.getTipo());
        response.setCompromiso(reporte.getCompromiso());
        response.setDescripcion(reporte.getDescripcion());
        response.setFecha(reporte.getFecha());
        response.setIdUsuario(reporte.getIdUsuario());
        response.setEstado(reporte.getEstado());

        return response;
    }
}
