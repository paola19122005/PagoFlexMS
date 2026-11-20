package com.pagoflex.agente.controller;

import com.pagoflex.agente.dto.request.ConversacionEstadoRequest;
import com.pagoflex.agente.dto.request.ConversacionRegistroRequest;
import com.pagoflex.agente.dto.request.MensajeRegistroRequest;
import com.pagoflex.agente.dto.response.ConversacionResponse;
import com.pagoflex.agente.dto.response.MensajeResponse;
import com.pagoflex.agente.service.ConversacionService;
import com.pagoflex.agente.service.MensajeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConversacionControllerTest {

    @Mock
    private ConversacionService conversacionService;
    @Mock
    private MensajeService mensajeService;

    @InjectMocks
    private ConversacionController controller;

    @Test
    void crear() {
        when(conversacionService.crear(any())).thenReturn(new ConversacionResponse());
        assertEquals(201, controller.crear(new ConversacionRegistroRequest()).getStatusCode().value());
    }

    @Test
    void listarTodos() {
        when(conversacionService.listarTodos()).thenReturn(List.of(new ConversacionResponse()));
        assertEquals(1, controller.listarTodos().size());
    }

    @Test
    void obtenerPorId() {
        when(conversacionService.obtenerPorId(1L)).thenReturn(new ConversacionResponse());
        controller.obtenerPorId(1L);
    }

    @Test
    void listarPorUsuario() {
        when(conversacionService.listarPorUsuario(1L)).thenReturn(List.of(new ConversacionResponse()));
        assertEquals(1, controller.listarPorUsuario(1L).size());
    }

    @Test
    void actualizarEstado() {
        when(conversacionService.actualizarEstado(eq(1L), any())).thenReturn(new ConversacionResponse());
        controller.actualizarEstado(1L, new ConversacionEstadoRequest());
    }

    @Test
    void listarMensajes() {
        when(mensajeService.listarPorConversacion(1L)).thenReturn(List.of(new MensajeResponse()));
        assertEquals(1, controller.listarMensajes(1L).size());
    }

    @Test
    void registrarMensaje() {
        when(mensajeService.registrar(eq(1L), any())).thenReturn(new MensajeResponse());
        assertEquals(201, controller.registrarMensaje(1L, new MensajeRegistroRequest()).getStatusCode().value());
    }
}
