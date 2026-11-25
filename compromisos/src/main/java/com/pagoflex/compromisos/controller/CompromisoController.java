package com.pagoflex.compromisos.controller;

import com.pagoflex.compromisos.dto.request.AnulacionRequest;
import com.pagoflex.compromisos.dto.request.CompromisoActualizacionPagoRequest;
import com.pagoflex.compromisos.dto.request.CompromisoActualizacionRequest;
import com.pagoflex.compromisos.dto.request.CompromisoActualizacionVencimientoRequest;
import com.pagoflex.compromisos.dto.request.CompromisoRegistroRequest;
import com.pagoflex.compromisos.dto.response.AnulacionResponse;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.service.CompromisoActualizacionPagoService;
import com.pagoflex.compromisos.service.CompromisoActualizacionRevisionService;
import com.pagoflex.compromisos.service.CompromisoActualizacionService;
import com.pagoflex.compromisos.service.CompromisoActualizacionVencimientoService;
import com.pagoflex.compromisos.service.CompromisoAnulacionService;
import com.pagoflex.compromisos.service.CompromisoConsultaService;
import com.pagoflex.compromisos.service.CompromisoRegistroResultado;
import com.pagoflex.compromisos.service.CompromisoRegistroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/compromisos")
public class CompromisoController {

    private final CompromisoRegistroService compromisoRegistroService;
    private final CompromisoConsultaService compromisoConsultaService;
    private final CompromisoActualizacionService compromisoActualizacionService;
    private final CompromisoAnulacionService compromisoAnulacionService;
    private final CompromisoActualizacionPagoService compromisoActualizacionPagoService;
    private final CompromisoActualizacionVencimientoService compromisoActualizacionVencimientoService;
    private final CompromisoActualizacionRevisionService compromisoActualizacionRevisionService;

    public CompromisoController(CompromisoRegistroService compromisoRegistroService,
                                CompromisoConsultaService compromisoConsultaService,
                                CompromisoActualizacionService compromisoActualizacionService,
                                CompromisoAnulacionService compromisoAnulacionService,
                                CompromisoActualizacionPagoService compromisoActualizacionPagoService,
                                CompromisoActualizacionVencimientoService compromisoActualizacionVencimientoService,
                                CompromisoActualizacionRevisionService compromisoActualizacionRevisionService) {
        this.compromisoRegistroService = compromisoRegistroService;
        this.compromisoConsultaService = compromisoConsultaService;
        this.compromisoActualizacionService = compromisoActualizacionService;
        this.compromisoAnulacionService = compromisoAnulacionService;
        this.compromisoActualizacionPagoService = compromisoActualizacionPagoService;
        this.compromisoActualizacionVencimientoService = compromisoActualizacionVencimientoService;
        this.compromisoActualizacionRevisionService = compromisoActualizacionRevisionService;
    }

    @PostMapping
    public ResponseEntity<CompromisoResponse> registrar(@Valid @RequestBody CompromisoRegistroRequest request) {
        CompromisoRegistroResultado resultado = compromisoRegistroService.registrar(request);
        HttpStatus status = resultado.creado() ? HttpStatus.CREATED : HttpStatus.OK;
        return ResponseEntity.status(status).body(resultado.respuesta());
    }

    @GetMapping("/{id}")
    public CompromisoResponse obtenerPorId(@PathVariable Integer id) {
        return compromisoConsultaService.obtenerPorId(id);
    }

    @GetMapping("/folio/{folio}")
    public CompromisoResponse obtenerPorFolio(@PathVariable String folio) {
        return compromisoConsultaService.obtenerPorFolio(folio);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<CompromisoResponse> listarPorUsuario(@PathVariable Integer usuarioId) {
        return compromisoConsultaService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<CompromisoResponse> listarPorEmpresa(@PathVariable Integer empresaId) {
        return compromisoConsultaService.listarPorEmpresa(empresaId);
    }

    @GetMapping("/usuario/{usuarioId}/empresa/{empresaId}")
    public List<CompromisoResponse> listarPorUsuarioYEmpresa(@PathVariable Integer usuarioId,
                                                            @PathVariable Integer empresaId) {
        return compromisoConsultaService.listarPorUsuarioYEmpresa(usuarioId, empresaId);
    }

    @GetMapping("/estado/{estado}")
    public List<CompromisoResponse> listarPorEstado(@PathVariable String estado) {
        return compromisoConsultaService.listarPorEstado(estado);
    }

    @GetMapping
    public List<CompromisoResponse> listarTodos() {
        return compromisoConsultaService.listarTodos();
    }

    @PutMapping("/{id}")
    public CompromisoResponse actualizar(@PathVariable Integer id,
                                        @Valid @RequestBody CompromisoActualizacionRequest request) {
        return compromisoActualizacionService.actualizar(id, request);
    }

    @PostMapping("/{id}/anulacion")
    public AnulacionResponse anular(@PathVariable Integer id,
                                   @Valid @RequestBody AnulacionRequest request) {
        return compromisoAnulacionService.anular(id, request);
    }

    @PutMapping("/{id}/pago")
    public CompromisoResponse actualizarPorPago(@PathVariable Integer id,
                                               @Valid @RequestBody CompromisoActualizacionPagoRequest request) {
        return compromisoActualizacionPagoService.actualizarPorPago(id, request);
    }

    @PutMapping("/{id}/vencimiento")
    public CompromisoResponse actualizarPorVencimiento(@PathVariable Integer id,
                                                      @Valid @RequestBody CompromisoActualizacionVencimientoRequest request) {
        return compromisoActualizacionVencimientoService.actualizarPorVencimiento(id, request);
    }

    @PutMapping("/{id}/revision")
    public CompromisoResponse actualizarRevision(@PathVariable Integer id) {
        return compromisoActualizacionRevisionService.actualizarRevision(id);
    }
}