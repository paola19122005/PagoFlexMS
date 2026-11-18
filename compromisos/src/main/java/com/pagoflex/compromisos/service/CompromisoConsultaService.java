package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompromisoConsultaService {

    private final CompromisoRepository compromisoRepository;
    private final EstadoCompromisoRepository estadoCompromisoRepository;
    private final CompromisoMapper compromisoMapper;

    public CompromisoConsultaService(CompromisoRepository compromisoRepository,
                                    EstadoCompromisoRepository estadoCompromisoRepository,
                                    CompromisoMapper compromisoMapper) {
        this.compromisoRepository = compromisoRepository;
        this.estadoCompromisoRepository = estadoCompromisoRepository;
        this.compromisoMapper = compromisoMapper;
    }

    @Transactional(readOnly = true)
    public CompromisoResponse obtenerPorId(Integer idCompromiso) {
        Compromiso compromiso = compromisoRepository.findById(idCompromiso)
                .orElseThrow(() -> new CompromisoNoEncontradoException(
                        "Compromiso no encontrado: " + idCompromiso));
        return compromisoMapper.toResponse(compromiso);
    }

    @Transactional(readOnly = true)
    public CompromisoResponse obtenerPorFolio(String folio) {
        Compromiso compromiso = compromisoRepository.findByFolio(folio)
                .orElseThrow(() -> new CompromisoNoEncontradoException(
                        "Compromiso no encontrado para el folio: " + folio));
        return compromisoMapper.toResponse(compromiso);
    }

    @Transactional(readOnly = true)
    public List<CompromisoResponse> listarPorUsuario(Integer idUsuarioFinal) {
        return compromisoRepository.findByIdUsuarioFinal(idUsuarioFinal).stream()
                .map(compromisoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompromisoResponse> listarPorEmpresa(Integer idEmpresa) {
        return compromisoRepository.findByIdEmpresa(idEmpresa).stream()
                .map(compromisoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompromisoResponse> listarPorUsuarioYEmpresa(Integer idUsuarioFinal, Integer idEmpresa) {
        return compromisoRepository.findByIdUsuarioFinalAndIdEmpresa(idUsuarioFinal, idEmpresa).stream()
                .map(compromisoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompromisoResponse> listarPorEstado(String nombreEstado) {
        EstadoCompromiso estado = estadoCompromisoRepository.findByNombre(nombreEstado)
                .orElseThrow(() -> new CompromisoNoEncontradoException(
                        "Estado no encontrado: " + nombreEstado));
        return compromisoRepository.findByEstado(estado).stream()
                .map(compromisoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CompromisoResponse> listarTodos() {
        return compromisoRepository.findAll().stream()
                .map(compromisoMapper::toResponse)
                .toList();
    }
}
