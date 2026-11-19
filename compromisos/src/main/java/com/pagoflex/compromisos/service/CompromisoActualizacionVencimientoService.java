package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoActualizacionVencimientoRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CompromisoActualizacionVencimientoService {

    private final CompromisoRepository compromisoRepository;
    private final EstadoCompromisoRepository estadoCompromisoRepository;
    private final CompromisoMapper compromisoMapper;

    public CompromisoActualizacionVencimientoService(CompromisoRepository compromisoRepository,
                                                    EstadoCompromisoRepository estadoCompromisoRepository,
                                                    CompromisoMapper compromisoMapper) {
        this.compromisoRepository = compromisoRepository;
        this.estadoCompromisoRepository = estadoCompromisoRepository;
        this.compromisoMapper = compromisoMapper;
    }

    @Transactional
    public CompromisoResponse actualizarPorVencimiento(Integer idCompromiso,
                                                      CompromisoActualizacionVencimientoRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud de actualización por vencimiento no puede ser nula");
        }
        if (request.getEstado() == null || request.getEstado().isBlank()) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        if (request.getEstado().length() > 30) {
            throw new IllegalArgumentException("El estado no puede superar 30 caracteres");
        }
        if (request.getRecargoAplicado() == null) {
            throw new IllegalArgumentException("El recargo aplicado es obligatorio");
        }
        if (request.getRecargoAplicado() < 0) {
            throw new IllegalArgumentException("El recargo aplicado no puede ser negativo");
        }

        Compromiso compromiso = compromisoRepository.findById(idCompromiso)
                .orElseThrow(() -> new CompromisoNoEncontradoException(
                        "Compromiso no encontrado: " + idCompromiso));

        EstadoCompromiso estadoNuevo = estadoCompromisoRepository
                .findByNombre(request.getEstado().trim())
                .orElseThrow(() -> new CompromisoPersistenciaException(
                        "Estado no encontrado: " + request.getEstado()));

        boolean cambioEstado = !estadoNuevo.getNombre().equals(compromiso.getEstado().getNombre());

        compromiso.setEstado(estadoNuevo);
        compromiso.setRecargoAplicado(request.getRecargoAplicado());

        if (cambioEstado) {
            compromiso.setFechaCambioEstado(LocalDateTime.now());
        }

        try {
            Compromiso actualizado = compromisoRepository.save(compromiso);
            return compromisoMapper.toResponse(actualizado);
        } catch (DataIntegrityViolationException ex) {
            throw new CompromisoPersistenciaException("Error al actualizar el compromiso por vencimiento", ex);
        }
    }
}
