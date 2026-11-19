package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CompromisoOrigenRecuperacionService {

    private final CompromisoRepository compromisoRepository;

    public CompromisoOrigenRecuperacionService(CompromisoRepository compromisoRepository) {
        this.compromisoRepository = compromisoRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public Optional<Compromiso> obtenerPorOrigen(String sistemaOrigen,
                                                String idInstanciaOrigen,
                                                Long idCompromisoOrigen) {
        return compromisoRepository.findBySistemaOrigenAndIdInstanciaOrigenAndIdCompromisoOrigen(
                sistemaOrigen, idInstanciaOrigen, idCompromisoOrigen);
    }
}
