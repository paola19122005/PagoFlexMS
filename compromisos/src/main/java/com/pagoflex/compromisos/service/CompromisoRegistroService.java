package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoRegistroRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.exception.ErrorConfiguracionCatalogoException;
import com.pagoflex.compromisos.exception.ViolacionIntegridadException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CompromisoRegistroService {

    private final CompromisoRepository compromisoRepository;
    private final EstadoCompromisoRepository estadoCompromisoRepository;
    private final CompromisoMapper compromisoMapper;

    public CompromisoRegistroService(CompromisoRepository compromisoRepository,
                                    EstadoCompromisoRepository estadoCompromisoRepository,
                                    CompromisoMapper compromisoMapper) {
        this.compromisoRepository = compromisoRepository;
        this.estadoCompromisoRepository = estadoCompromisoRepository;
        this.compromisoMapper = compromisoMapper;
    }

    @Transactional
    public CompromisoResponse registrar(CompromisoRegistroRequest request) {
        EstadoCompromiso estadoInicial = estadoCompromisoRepository.findByNombre("Pendiente")
                .orElseThrow(() -> new ErrorConfiguracionCatalogoException("No se encontró el estado inicial Pendiente"));

        Compromiso compromiso = compromisoMapper.toEntity(request);
        compromiso.setEstado(estadoInicial);
        compromiso.setFolio(generarFolioUnico());
        LocalDateTime ahora = LocalDateTime.now();
        compromiso.setFechaRegistro(ahora);
        compromiso.setFechaCambioEstado(ahora);

        try {
            Compromiso guardado = compromisoRepository.saveAndFlush(compromiso);
            if (guardado.getIdCompromiso() == null) {
                throw new CompromisoPersistenciaException("No se obtuvo el identificador generado por MySQL");
            }

            String folioDefinitivo = String.format("CP-%d-%04d",
                    LocalDate.now().getYear(), guardado.getIdCompromiso());
            guardado.setFolio(folioDefinitivo);

            Compromiso actualizado = compromisoRepository.save(guardado);
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
            throw new CompromisoPersistenciaException("Error al guardar el compromiso", ex);
        }
    }

    private String generarFolioUnico() {
        long siguiente = compromisoRepository.count() + 1;
        String folio;
        do {
            folio = String.format("CP-%d-%04d", LocalDate.now().getYear(), siguiente++);
        } while (compromisoRepository.existsByFolio(folio));
        return folio;
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