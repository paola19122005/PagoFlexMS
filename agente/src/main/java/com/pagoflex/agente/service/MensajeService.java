package com.pagoflex.agente.service;

import com.pagoflex.agente.dto.request.MensajeRegistroRequest;
import com.pagoflex.agente.dto.response.MensajeResponse;
import com.pagoflex.agente.entity.Mensaje;
import com.pagoflex.agente.exception.ConversacionNoEncontradaException;
import com.pagoflex.agente.mapper.MensajeMapper;
import com.pagoflex.agente.repository.ConversacionRepository;
import com.pagoflex.agente.repository.MensajeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final ConversacionRepository conversacionRepository;
    private final MensajeMapper mensajeMapper;

    public MensajeService(MensajeRepository mensajeRepository,
                          ConversacionRepository conversacionRepository,
                          MensajeMapper mensajeMapper) {
        this.mensajeRepository = mensajeRepository;
        this.conversacionRepository = conversacionRepository;
        this.mensajeMapper = mensajeMapper;
    }

    @Transactional(readOnly = true)
    public List<MensajeResponse> listarPorConversacion(Long idConversacion) {
        if (!conversacionRepository.existsById(idConversacion)) {
            throw new ConversacionNoEncontradaException("Conversacion no encontrada: " + idConversacion);
        }
        return mensajeRepository.findByIdConversacionOrderByIdAsc(idConversacion).stream()
                .map(mensajeMapper::toResponse)
                .toList();
    }

    @Transactional
    public MensajeResponse registrar(Long idConversacion, MensajeRegistroRequest request) {
        if (!conversacionRepository.existsById(idConversacion)) {
            throw new ConversacionNoEncontradaException("Conversacion no encontrada: " + idConversacion);
        }
        Mensaje mensaje = mensajeMapper.toEntity(idConversacion, request);
        Mensaje guardado = mensajeRepository.save(mensaje);
        return mensajeMapper.toResponse(guardado);
    }
}
