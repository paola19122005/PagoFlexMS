package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoRegistroRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

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
                .orElseThrow(() -> new CompromisoPersistenciaException("Estado inicial no encontrado"));

        Compromiso compromiso = compromisoMapper.toEntity(request);
        compromiso.setEstado(estadoInicial);
        compromiso.setFolio(generarFolioTemporal());
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
            throw new CompromisoPersistenciaException("Error al guardar el compromiso", ex);
        }
    }

    private String generarFolioTemporal() {
        return "TMP-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
