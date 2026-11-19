package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.AnulacionRequest;
import com.pagoflex.compromisos.dto.response.AnulacionResponse;
import com.pagoflex.compromisos.entity.Anulacion;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.entity.MotivoAnulacion;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.CompromisoYaAnuladoException;
import com.pagoflex.compromisos.exception.ErrorConfiguracionCatalogoException;
import com.pagoflex.compromisos.exception.MotivoAnulacionNoEncontradoException;
import com.pagoflex.compromisos.repository.AnulacionRepository;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import com.pagoflex.compromisos.repository.MotivoAnulacionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CompromisoAnulacionService {

    private final CompromisoRepository compromisoRepository;
    private final AnulacionRepository anulacionRepository;
    private final MotivoAnulacionRepository motivoAnulacionRepository;
    private final EstadoCompromisoRepository estadoCompromisoRepository;

    public CompromisoAnulacionService(CompromisoRepository compromisoRepository,
                                     AnulacionRepository anulacionRepository,
                                     MotivoAnulacionRepository motivoAnulacionRepository,
                                     EstadoCompromisoRepository estadoCompromisoRepository) {
        this.compromisoRepository = compromisoRepository;
        this.anulacionRepository = anulacionRepository;
        this.motivoAnulacionRepository = motivoAnulacionRepository;
        this.estadoCompromisoRepository = estadoCompromisoRepository;
    }

    @Transactional
    public AnulacionResponse anular(Integer idCompromiso, AnulacionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud de anulación no puede ser nula");
        }
        if (request.getIdEjecutivo() == null) {
            throw new IllegalArgumentException("El identificador del ejecutivo es obligatorio");
        }
        if (request.getIdEjecutivo() <= 0) {
            throw new IllegalArgumentException("El identificador del ejecutivo debe ser mayor que cero");
        }
        if (request.getMotivo() == null) {
            throw new IllegalArgumentException("El motivo de anulación es obligatorio");
        }
        if (request.getMotivo().isBlank()) {
            throw new IllegalArgumentException("El motivo de anulación no puede estar vacío ni contener solo espacios");
        }
        if (request.getMotivo().length() > 255) {
            throw new IllegalArgumentException("El motivo de anulación no puede superar 255 caracteres");
        }

        Compromiso compromiso = compromisoRepository.findById(idCompromiso)
                .orElseThrow(() -> new CompromisoNoEncontradoException(
                        "Compromiso no encontrado: " + idCompromiso));

        if (anulacionRepository.existsById(idCompromiso)) {
            throw new CompromisoYaAnuladoException("El compromiso " + idCompromiso + " ya tiene una anulación registrada");
        }

        EstadoCompromiso estadoAnulado = estadoCompromisoRepository.findByNombre("Anulado")
                .orElseThrow(() -> new ErrorConfiguracionCatalogoException("No se encontró el estado Anulado"));

        String motivoRecibido = request.getMotivo().trim();
        String motivoOriginal = request.getMotivo();

        MotivoAnulacion motivoCatalogo;
        String comentario;

        Optional<MotivoAnulacion> motivoEncontrado =
                motivoAnulacionRepository.findByNombre(motivoRecibido);

        if (motivoEncontrado.isPresent()
                && !"Otro".equals(motivoEncontrado.get().getNombre())) {
            motivoCatalogo = motivoEncontrado.get();
            comentario = null;
        } else {
            motivoCatalogo = motivoAnulacionRepository.findByNombre("Otro")
                    .orElseThrow(() -> new MotivoAnulacionNoEncontradoException("No se encontró el motivo Otro"));
            comentario = motivoOriginal;
        }

        Anulacion anulacion = new Anulacion();
        anulacion.setCompromiso(compromiso);
        anulacion.setIdEjecutivo(request.getIdEjecutivo());
        anulacion.setMotivo(motivoCatalogo);
        anulacion.setComentario(comentario);
        anulacion.setFecha(LocalDateTime.now());

        try {
            Anulacion anulacionGuardada = anulacionRepository.saveAndFlush(anulacion);
            compromiso.setEstado(estadoAnulado);
            compromisoRepository.save(compromiso);

            AnulacionResponse response = new AnulacionResponse();
            response.setIdCompromiso(anulacionGuardada.getIdCompromiso());
            response.setIdEjecutivo(anulacionGuardada.getIdEjecutivo());
            response.setMotivo(anulacionGuardada.getMotivo().getNombre());
            response.setComentario(anulacionGuardada.getComentario());
            response.setFecha(anulacionGuardada.getFecha());
            response.setEstadoCompromiso(compromiso.getEstado().getNombre());
            return response;
        } catch (DataIntegrityViolationException ex) {
            throw new CompromisoYaAnuladoException("El compromiso " + idCompromiso + " ya tiene una anulación registrada");
        }
    }
}