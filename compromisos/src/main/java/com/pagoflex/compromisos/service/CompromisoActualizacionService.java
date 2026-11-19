package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoActualizacionRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompromisoActualizacionService {

    private final CompromisoRepository compromisoRepository;
    private final CompromisoMapper compromisoMapper;

    public CompromisoActualizacionService(CompromisoRepository compromisoRepository,
                                         CompromisoMapper compromisoMapper) {
        this.compromisoRepository = compromisoRepository;
        this.compromisoMapper = compromisoMapper;
    }

    @Transactional
    public CompromisoResponse actualizar(Integer idCompromiso, CompromisoActualizacionRequest request) {
        Compromiso compromiso = compromisoRepository.findById(idCompromiso)
                .orElseThrow(() -> new CompromisoNoEncontradoException(
                        "Compromiso no encontrado: " + idCompromiso));

        compromisoMapper.actualizarEntidad(compromiso, request);

        try {
            Compromiso actualizado = compromisoRepository.save(compromiso);
            return compromisoMapper.toResponse(actualizado);
        } catch (DataIntegrityViolationException ex) {
            throw new CompromisoPersistenciaException("Error al actualizar el compromiso", ex);
        }
    }
}
