package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoActualizacionRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.exception.ViolacionIntegridadException;
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
            String restriccion = obtenerRestriccion(ex);
            if ("uk_compromiso_folio".equalsIgnoreCase(restriccion)) {
                throw new ViolacionIntegridadException("FOLIO_DUPLICADO",
                        "Ya existe un compromiso con el folio " + compromiso.getFolio(), ex);
            }
            if ("uk_compromiso_unicidad".equalsIgnoreCase(restriccion)) {
                throw new ViolacionIntegridadException("COMPROMISO_DUPLICADO",
                        "Ya existe un compromiso con la misma combinación de usuario, empresa, concepto y cuota", ex);
            }
            throw new CompromisoPersistenciaException("Error al actualizar el compromiso", ex);
        }
    }

    private String obtenerRestriccion(org.springframework.dao.DataIntegrityViolationException ex) {
        Throwable causa = ex;
        while (causa != null) {
            if (causa instanceof org.hibernate.exception.ConstraintViolationException hibernateEx) {
                return hibernateEx.getConstraintName();
            }
            causa = causa.getCause();
        }
        return null;
    }
}