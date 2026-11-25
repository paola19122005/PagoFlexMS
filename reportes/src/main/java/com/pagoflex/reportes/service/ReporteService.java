package com.pagoflex.reportes.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pagoflex.reportes.dto.request.ReporteEstadoRequest;
import com.pagoflex.reportes.dto.request.ReporteRegistroRequest;
import com.pagoflex.reportes.dto.response.ReporteResponse;
import com.pagoflex.reportes.entity.Reporte;
import com.pagoflex.reportes.entity.ReporteMensaje;
import com.pagoflex.reportes.exception.ReporteNoEncontradoException;
import com.pagoflex.reportes.mapper.ReporteMapper;
import com.pagoflex.reportes.repository.ReporteMensajeRepository;
import com.pagoflex.reportes.repository.ReporteRepository;

@Service
public class ReporteService {

    private static final Set<String> ESTADOS_VALIDOS = 
            Set.of("PENDIENTE", "EN_PROCESO", "FINALIZADO");
    
    private final ReporteRepository reporteRepository;
    private final ReporteMensajeRepository mensajeRepository;
    private final ReporteMapper reporteMapper;

    public ReporteService(
            ReporteRepository reporteRepository,
            ReporteMensajeRepository mensajeRepository,
            ReporteMapper reporteMapper) {
        
        this.reporteRepository = reporteRepository;
        this.mensajeRepository = mensajeRepository;
        this.reporteMapper = reporteMapper;
    }

    @Transactional
    public ReporteResponse crear(ReporteRegistroRequest request) {

        Reporte reporte = reporteMapper.toEntity(request);

        Reporte guardado = reporteRepository.save(reporte);

        ReporteMensaje mensajeInicial = new ReporteMensaje();

        mensajeInicial.setIdReporte(guardado.getId());
        mensajeInicial.setIdRemitente(guardado.getIdUsuario());
        mensajeInicial.setRolRemitente("USUARIO");
        mensajeInicial.setContenido(guardado.getDescripcion());
        mensajeInicial.setFechaHora(guardado.getFecha());

        mensajeRepository.save(mensajeInicial);

        return reporteMapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<ReporteResponse> listarTodos() {

        return reporteRepository
                .findAllByOrderByIdDesc()
                .stream()
                .map(reporteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReporteResponse> listarPorUsuario(Long idUsuario) {

        return reporteRepository
                .findByIdUsuarioOrderByIdDesc(idUsuario)
                .stream()
                .map(reporteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReporteResponse obtenerPorId(Long id) {

        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() ->
                        new ReporteNoEncontradoException(
                                "Reporte no encontrado: " + id
                        )
                );

        return reporteMapper.toResponse(reporte);
    }

    @Transactional
    public ReporteResponse actualizarEstado(
            Long id,
            ReporteEstadoRequest request) {

    
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() ->
                        new ReporteNoEncontradoException(
                                "Reporte no encontrado: " + id
                        )
                );

        String estado = request.getEstado().toUpperCase();

        if (!ESTADOS_VALIDOS.contains(estado)) {
            throw new IllegalArgumentException(
                    "Estado no válido. Valores permitidos: "
                            + ESTADOS_VALIDOS
            );
        }

        reporte.setEstado(estado);

        Reporte guardado = reporteRepository.save(reporte);

        return reporteMapper.toResponse(guardado);
    }
}
