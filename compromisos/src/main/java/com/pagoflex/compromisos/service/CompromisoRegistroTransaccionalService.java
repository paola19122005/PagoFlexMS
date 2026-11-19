package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoRegistroRequest;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.exception.ErrorConfiguracionCatalogoException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CompromisoRegistroTransaccionalService {

    private final CompromisoRepository compromisoRepository;
    private final EstadoCompromisoRepository estadoCompromisoRepository;
    private final CompromisoMapper compromisoMapper;

    public CompromisoRegistroTransaccionalService(CompromisoRepository compromisoRepository,
                                                 EstadoCompromisoRepository estadoCompromisoRepository,
                                                 CompromisoMapper compromisoMapper) {
        this.compromisoRepository = compromisoRepository;
        this.estadoCompromisoRepository = estadoCompromisoRepository;
        this.compromisoMapper = compromisoMapper;
    }

    @Transactional
    public CompromisoRegistroResultado registrar(CompromisoRegistroRequest request) {
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
            return new CompromisoRegistroResultado(true, compromisoMapper.toResponse(actualizado));
        } catch (DataIntegrityViolationException ex) {
            throw ex;
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
}
