package com.pagoflex.agente.service;

import com.pagoflex.agente.dto.request.ConversacionEstadoRequest;
import com.pagoflex.agente.dto.request.ConversacionRegistroRequest;
import com.pagoflex.agente.dto.response.ConversacionResponse;
import com.pagoflex.agente.entity.Conversacion;
import com.pagoflex.agente.exception.ConversacionNoEncontradaException;
import com.pagoflex.agente.mapper.ConversacionMapper;
import com.pagoflex.agente.repository.ConversacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConversacionService {

    private final ConversacionRepository conversacionRepository;
    private final ConversacionMapper conversacionMapper;

    public ConversacionService(ConversacionRepository conversacionRepository,
                               ConversacionMapper conversacionMapper) {
        this.conversacionRepository = conversacionRepository;
        this.conversacionMapper = conversacionMapper;
    }

    @Transactional
    public ConversacionResponse crear(ConversacionRegistroRequest request) {
        Conversacion conversacion = conversacionMapper.toEntity(request);
        Conversacion guardada = conversacionRepository.save(conversacion);
        return conversacionMapper.toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public ConversacionResponse obtenerPorId(Long id) {
        Conversacion conversacion = conversacionRepository.findById(id)
                .orElseThrow(() -> new ConversacionNoEncontradaException(
                        "Conversacion no encontrada: " + id));
        return conversacionMapper.toResponse(conversacion);
    }

    @Transactional(readOnly = true)
    public List<ConversacionResponse> listarTodos() {
        return conversacionRepository.findAll().stream()
                .map(conversacionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConversacionResponse> listarPorUsuario(Long idUsuario) {
        return conversacionRepository.findByIdUsuario(idUsuario).stream()
                .map(conversacionMapper::toResponse)
                .toList();
    }

    @Transactional
    public ConversacionResponse actualizarEstado(Long id, ConversacionEstadoRequest request) {
        Conversacion conversacion = conversacionRepository.findById(id)
                .orElseThrow(() -> new ConversacionNoEncontradaException(
                        "Conversacion no encontrada: " + id));
        conversacion.setEstado(request.getEstado());
        Conversacion guardada = conversacionRepository.save(conversacion);
        return conversacionMapper.toResponse(guardada);
    }
}
