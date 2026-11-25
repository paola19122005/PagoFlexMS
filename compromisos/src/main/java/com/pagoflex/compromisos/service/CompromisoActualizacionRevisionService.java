package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.exception.ErrorConfiguracionCatalogoException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CompromisoActualizacionRevisionService {

    private final CompromisoRepository compromisoRepository;
    private final EstadoCompromisoRepository estadoCompromisoRepository;
    private final CompromisoMapper compromisoMapper;

    public CompromisoActualizacionRevisionService(CompromisoRepository compromisoRepository,
                                                 EstadoCompromisoRepository estadoCompromisoRepository,
                                                 CompromisoMapper compromisoMapper) {
        this.compromisoRepository = compromisoRepository;
        this.estadoCompromisoRepository = estadoCompromisoRepository;
        this.compromisoMapper = compromisoMapper;
    }

    @Transactional
    public CompromisoResponse actualizarRevision(Integer idCompromiso) {
        Compromiso compromiso = compromisoRepository.findById(idCompromiso)
                .orElseThrow(() -> new CompromisoNoEncontradoException(
                        "Compromiso no encontrado: " + idCompromiso));

        EstadoCompromiso estadoRevision = estadoCompromisoRepository.findByNombre("En revisión")
                .orElseThrow(() -> new ErrorConfiguracionCatalogoException("No se encontró el estado En revisión"));

        boolean cambioEstado = !estadoRevision.getNombre().equals(compromiso.getEstado().getNombre());

        compromiso.setEstado(estadoRevision);
        if (cambioEstado) {
            compromiso.setFechaCambioEstado(LocalDateTime.now());
        }

        try {
            Compromiso actualizado = compromisoRepository.save(compromiso);
            return compromisoMapper.toResponse(actualizado);
        } catch (DataIntegrityViolationException ex) {
            throw new CompromisoPersistenciaException("Error al actualizar el compromiso a revisión", ex);
        }
    }
}
