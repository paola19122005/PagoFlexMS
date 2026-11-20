package com.pagoflex.agente.service;

import com.pagoflex.agente.dto.request.ConversacionEstadoRequest;
import com.pagoflex.agente.dto.request.ConversacionRegistroRequest;
import com.pagoflex.agente.dto.response.ConversacionResponse;
import com.pagoflex.agente.entity.Conversacion;
import com.pagoflex.agente.exception.ConversacionNoEncontradaException;
import com.pagoflex.agente.mapper.ConversacionMapper;
import com.pagoflex.agente.repository.ConversacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversacionServiceTest {

    @Mock
    private ConversacionRepository conversacionRepository;
    @Mock
    private ConversacionMapper conversacionMapper;

    @InjectMocks
    private ConversacionService service;

    @Test
    void crear() {
        ConversacionRegistroRequest request = new ConversacionRegistroRequest();
        Conversacion conversacion = new Conversacion();
        Conversacion guardada = new Conversacion();
        ConversacionResponse response = new ConversacionResponse();
        when(conversacionMapper.toEntity(request)).thenReturn(conversacion);
        when(conversacionRepository.save(conversacion)).thenReturn(guardada);
        when(conversacionMapper.toResponse(guardada)).thenReturn(response);
        assertEquals(response, service.crear(request));
    }

    @Test
    void obtenerPorIdEncontrada() {
        Conversacion conversacion = new Conversacion();
        ConversacionResponse response = new ConversacionResponse();
        when(conversacionRepository.findById(1L)).thenReturn(Optional.of(conversacion));
        when(conversacionMapper.toResponse(conversacion)).thenReturn(response);
        assertEquals(response, service.obtenerPorId(1L));
    }

    @Test
    void obtenerPorIdNoEncontrada() {
        when(conversacionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ConversacionNoEncontradaException.class, () -> service.obtenerPorId(1L));
    }

    @Test
    void listarTodos() {
        Conversacion conversacion = new Conversacion();
        when(conversacionRepository.findAll()).thenReturn(List.of(conversacion));
        when(conversacionMapper.toResponse(any())).thenReturn(new ConversacionResponse());
        assertEquals(1, service.listarTodos().size());
    }

    @Test
    void listarPorUsuario() {
        Conversacion conversacion = new Conversacion();
        when(conversacionRepository.findByIdUsuario(5L)).thenReturn(List.of(conversacion));
        when(conversacionMapper.toResponse(any())).thenReturn(new ConversacionResponse());
        assertEquals(1, service.listarPorUsuario(5L).size());
    }

    @Test
    void actualizarEstadoEncontrada() {
        Conversacion conversacion = new Conversacion();
        Conversacion guardada = new Conversacion();
        ConversacionResponse response = new ConversacionResponse();
        ConversacionEstadoRequest request = new ConversacionEstadoRequest();
        request.setEstado("FINALIZADO");
        when(conversacionRepository.findById(1L)).thenReturn(Optional.of(conversacion));
        when(conversacionRepository.save(conversacion)).thenReturn(guardada);
        when(conversacionMapper.toResponse(guardada)).thenReturn(response);
        assertEquals(response, service.actualizarEstado(1L, request));
    }

    @Test
    void actualizarEstadoNoEncontrada() {
        ConversacionEstadoRequest request = new ConversacionEstadoRequest();
        request.setEstado("FINALIZADO");
        when(conversacionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ConversacionNoEncontradaException.class, () -> service.actualizarEstado(1L, request));
    }
}
