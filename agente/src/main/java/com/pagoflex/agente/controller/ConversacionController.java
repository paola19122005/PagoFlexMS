package com.pagoflex.agente.controller;

import com.pagoflex.agente.dto.request.ConversacionEstadoRequest;
import com.pagoflex.agente.dto.request.ConversacionRegistroRequest;
import com.pagoflex.agente.dto.request.MensajeRegistroRequest;
import com.pagoflex.agente.dto.response.ConversacionResponse;
import com.pagoflex.agente.dto.response.MensajeResponse;
import com.pagoflex.agente.service.ConversacionService;
import com.pagoflex.agente.service.MensajeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversaciones")
public class ConversacionController {

    private final ConversacionService conversacionService;
    private final MensajeService mensajeService;

    public ConversacionController(ConversacionService conversacionService,
                                 MensajeService mensajeService) {
        this.conversacionService = conversacionService;
        this.mensajeService = mensajeService;
    }

    @PostMapping
    public ResponseEntity<ConversacionResponse> crear(@Valid @RequestBody ConversacionRegistroRequest request) {
        ConversacionResponse response = conversacionService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<ConversacionResponse> listarTodos() {
        return conversacionService.listarTodos();
    }

    @GetMapping("/{id}")
    public ConversacionResponse obtenerPorId(@PathVariable Long id) {
        return conversacionService.obtenerPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<ConversacionResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return conversacionService.listarPorUsuario(usuarioId);
    }

    @PutMapping("/{id}/estado")
    public ConversacionResponse actualizarEstado(@PathVariable Long id,
                                                @Valid @RequestBody ConversacionEstadoRequest request) {
        return conversacionService.actualizarEstado(id, request);
    }

    @GetMapping("/{id}/mensajes")
    public List<MensajeResponse> listarMensajes(@PathVariable Long id) {
        return mensajeService.listarPorConversacion(id);
    }

    @PostMapping("/{id}/mensajes")
    public ResponseEntity<MensajeResponse> registrarMensaje(@PathVariable Long id,
                                                            @Valid @RequestBody MensajeRegistroRequest request) {
        MensajeResponse response = mensajeService.registrar(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
