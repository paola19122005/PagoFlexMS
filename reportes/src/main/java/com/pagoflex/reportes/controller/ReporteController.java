package com.pagoflex.reportes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagoflex.reportes.dto.request.ReporteEstadoRequest;
import com.pagoflex.reportes.dto.request.ReporteMensajeRequest;
import com.pagoflex.reportes.dto.request.ReporteRegistroRequest;
import com.pagoflex.reportes.dto.response.ReporteMensajeResponse;
import com.pagoflex.reportes.dto.response.ReporteResponse;
import com.pagoflex.reportes.service.ReporteMensajeService;
import com.pagoflex.reportes.service.ReporteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    private final ReporteService reporteService;
    private final ReporteMensajeService mensajeService;

    public ReporteController(
            ReporteService reporteService,
            ReporteMensajeService mensajeService) {

        this.reporteService = reporteService;
        this.mensajeService = mensajeService;
    }

    @PostMapping
    public ResponseEntity<ReporteResponse> crear(
            @Valid @RequestBody ReporteRegistroRequest request) {

        ReporteResponse response =
                reporteService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<ReporteResponse> listarTodos() {

        return reporteService.listarTodos();
    }

    @GetMapping("/{id}")
    public ReporteResponse obtenerPorId(
            @PathVariable Long id) {

        return reporteService.obtenerPorId(id);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<ReporteResponse> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return reporteService.listarPorUsuario(usuarioId);
    }

    @PutMapping("/{id}/estado")
    public ReporteResponse actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody ReporteEstadoRequest request) {

        return reporteService.actualizarEstado(id, request);
    }

    @GetMapping("/{id}/mensajes")
    public List<ReporteMensajeResponse> listarMensajes(
            @PathVariable Long id) {

        return mensajeService.listarPorReporte(id);
    }

    @PostMapping("/{id}/mensajes")
    public ResponseEntity<ReporteMensajeResponse> registrarMensaje(
            @PathVariable Long id,
            @Valid @RequestBody ReporteMensajeRequest request) {

        ReporteMensajeResponse response =
                mensajeService.registrar(id, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}
