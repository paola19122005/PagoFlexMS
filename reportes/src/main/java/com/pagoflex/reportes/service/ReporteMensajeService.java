package com.pagoflex.reportes.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pagoflex.reportes.dto.request.ReporteMensajeRequest;
import com.pagoflex.reportes.dto.response.ReporteMensajeResponse;
import com.pagoflex.reportes.entity.ReporteMensaje;
import com.pagoflex.reportes.exception.ReporteNoEncontradoException;
import com.pagoflex.reportes.mapper.ReporteMensajeMapper;
import com.pagoflex.reportes.repository.ReporteMensajeRepository;
import com.pagoflex.reportes.repository.ReporteRepository;

@Service
public class ReporteMensajeService {

    private static final Set<String> ROLES_VALIDOS =
            Set.of("USUARIO", "AGENTE");

    private final ReporteMensajeRepository mensajeRepository;
    private final ReporteRepository reporteRepository;
    private final ReporteMensajeMapper mensajeMapper;

    public ReporteMensajeService(
            ReporteMensajeRepository mensajeRepository,
            ReporteRepository reporteRepository,
            ReporteMensajeMapper mensajeMapper) {

        this.mensajeRepository = mensajeRepository;
        this.reporteRepository = reporteRepository;
        this.mensajeMapper = mensajeMapper;
    }

    @Transactional(readOnly = true)
    public List<ReporteMensajeResponse> listarPorReporte(
            Long idReporte) {

        if (!reporteRepository.existsById(idReporte)) {

            throw new ReporteNoEncontradoException(
                    "Reporte no encontrado: " + idReporte
            );
        }

        return mensajeRepository
                .findByIdReporteOrderByIdAsc(idReporte)
                .stream()
                .map(mensajeMapper::toResponse)
                .toList();
    }

    @Transactional
    public ReporteMensajeResponse registrar(
            Long idReporte,
            ReporteMensajeRequest request) {

        if (!reporteRepository.existsById(idReporte)) {

            throw new ReporteNoEncontradoException(
                    "Reporte no encontrado: " + idReporte
            );
        }

        String rol = request.getRolRemitente().toUpperCase();

        if (!ROLES_VALIDOS.contains(rol)) {

            throw new IllegalArgumentException(
                    "Rol no válido. Valores permitidos: "
                            + ROLES_VALIDOS
            );
        }

        request.setRolRemitente(rol);

        ReporteMensaje mensaje =
                mensajeMapper.toEntity(idReporte, request);

        ReporteMensaje guardado =
                mensajeRepository.save(mensaje);

        return mensajeMapper.toResponse(guardado);
    }
}
