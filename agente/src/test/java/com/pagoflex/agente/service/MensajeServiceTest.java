package com.pagoflex.agente.service;

import com.pagoflex.agente.dto.request.MensajeRegistroRequest;
import com.pagoflex.agente.dto.response.MensajeResponse;
import com.pagoflex.agente.entity.Mensaje;
import com.pagoflex.agente.exception.ConversacionNoEncontradaException;
import com.pagoflex.agente.mapper.MensajeMapper;
import com.pagoflex.agente.repository.ConversacionRepository;
import com.pagoflex.agente.repository.MensajeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MensajeServiceTest {

    @Mock
    private MensajeRepository mensajeRepository;
    @Mock
    private ConversacionRepository conversacionRepository;
    @Mock
    private MensajeMapper mensajeMapper;

    @InjectMocks
    private MensajeService service;

    @Test
    void listarPorConversacionExiste() {
        Mensaje mensaje = new Mensaje();
        when(conversacionRepository.existsById(1L)).thenReturn(true);
        when(mensajeRepository.findByIdConversacionOrderByIdAsc(1L)).thenReturn(List.of(mensaje));
        when(mensajeMapper.toResponse(any())).thenReturn(new MensajeResponse());
        assertEquals(1, service.listarPorConversacion(1L).size());
    }

    @Test
    void listarPorConversacionNoExiste() {
        when(conversacionRepository.existsById(1L)).thenReturn(false);
        assertThrows(ConversacionNoEncontradaException.class, () -> service.listarPorConversacion(1L));
    }

    @Test
    void registrarExiste() {
        MensajeRegistroRequest request = new MensajeRegistroRequest();
        Mensaje mensaje = new Mensaje();
        Mensaje guardado = new Mensaje();
        MensajeResponse response = new MensajeResponse();
        when(conversacionRepository.existsById(1L)).thenReturn(true);
        when(mensajeMapper.toEntity(1L, request)).thenReturn(mensaje);
        when(mensajeRepository.save(mensaje)).thenReturn(guardado);
        when(mensajeMapper.toResponse(guardado)).thenReturn(response);
        assertEquals(response, service.registrar(1L, request));
    }

    @Test
    void registrarNoExiste() {
        MensajeRegistroRequest request = new MensajeRegistroRequest();
        when(conversacionRepository.existsById(1L)).thenReturn(false);
        assertThrows(ConversacionNoEncontradaException.class, () -> service.registrar(1L, request));
    }
}
