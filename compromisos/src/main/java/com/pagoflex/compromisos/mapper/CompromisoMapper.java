package com.pagoflex.compromisos.mapper;

import com.pagoflex.compromisos.dto.request.CompromisoActualizacionRequest;
import com.pagoflex.compromisos.dto.request.CompromisoRegistroRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import org.springframework.stereotype.Component;

@Component
public class CompromisoMapper {

    public Compromiso toEntity(CompromisoRegistroRequest request) {
        Compromiso compromiso = new Compromiso();
        compromiso.setIdUsuarioFinal(request.getIdUsuarioFinal());
        compromiso.setIdEmpresa(request.getIdEmpresa());
        compromiso.setConcepto(request.getConcepto());
        compromiso.setNumeroCuota(request.getNumeroCuota());
        compromiso.setTotalCuotas(request.getTotalCuotas());
        compromiso.setMonto(request.getMonto());
        compromiso.setFechaVencimiento(request.getFechaVencimiento());
        compromiso.setIdEjecutivoRegistro(request.getIdEjecutivoRegistro());
        compromiso.setRecargoAplicado(0);
        return compromiso;
    }

    public void actualizarEntidad(Compromiso compromiso, CompromisoActualizacionRequest request) {
        compromiso.setConcepto(request.getConcepto());
        compromiso.setNumeroCuota(request.getNumeroCuota());
        compromiso.setTotalCuotas(request.getTotalCuotas());
        compromiso.setMonto(request.getMonto());
        compromiso.setFechaVencimiento(request.getFechaVencimiento());
    }

    public CompromisoResponse toResponse(Compromiso compromiso) {
        CompromisoResponse response = new CompromisoResponse();
        response.setIdCompromiso(compromiso.getIdCompromiso());
        response.setFolio(compromiso.getFolio());
        response.setIdUsuarioFinal(compromiso.getIdUsuarioFinal());
        response.setIdEmpresa(compromiso.getIdEmpresa());
        response.setConcepto(compromiso.getConcepto());
        response.setNumeroCuota(compromiso.getNumeroCuota());
        response.setTotalCuotas(compromiso.getTotalCuotas());
        response.setMonto(compromiso.getMonto());
        response.setFechaVencimiento(compromiso.getFechaVencimiento());
        response.setEstado(compromiso.getEstado() != null ? compromiso.getEstado().getNombre() : null);
        response.setRecargoAplicado(compromiso.getRecargoAplicado());
        response.setFechaCambioEstado(compromiso.getFechaCambioEstado());
        response.setIdEjecutivoRegistro(compromiso.getIdEjecutivoRegistro());
        response.setFechaRegistro(compromiso.getFechaRegistro());
        return response;
    }
}
