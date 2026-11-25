package com.pagoflex.pagos.controller;

import com.pagoflex.pagos.dto.request.PagoRegistroRequest;
import com.pagoflex.pagos.dto.response.ComprobanteResponse;
import com.pagoflex.pagos.dto.response.PagoResumenResponse;
import com.pagoflex.pagos.dto.response.PagoResponse;
import com.pagoflex.pagos.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @PostMapping
    public ResponseEntity<PagoResponse> registrar(@Valid @RequestBody PagoRegistroRequest request) {
        PagoResponse response = pagoService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public List<PagoResponse> listarTodos() {
        return pagoService.listarTodos();
    }

    @GetMapping("/{id}")
    public PagoResponse obtenerPorId(@PathVariable Long id) {
        return pagoService.obtenerPorId(id);
    }

    @GetMapping("/folio/{folioUnico}")
    public PagoResponse obtenerPorFolioUnico(@PathVariable String folioUnico) {
        return pagoService.obtenerPorFolioUnico(folioUnico);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<PagoResponse> listarPorUsuario(@PathVariable Long usuarioId) {
        return pagoService.listarPorUsuario(usuarioId);
    }

    @GetMapping("/compromiso/{compromisoId}")
    public List<PagoResponse> listarPorCompromiso(@PathVariable Long compromisoId) {
        return pagoService.listarPorCompromiso(compromisoId);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<PagoResponse> listarPorEmpresa(@PathVariable Long empresaId) {
        return pagoService.listarPorEmpresa(empresaId);
    }

    @GetMapping("/empresa/{empresaId}/resumen")
    public PagoResumenResponse resumenPorEmpresa(@PathVariable Long empresaId) {
        return pagoService.resumenPorEmpresa(empresaId);
    }

    @GetMapping("/{id}/comprobante")
    public ComprobanteResponse obtenerComprobantePorPago(@PathVariable Long id) {
        return pagoService.obtenerComprobantePorPago(id);
    }

    @GetMapping("/folio/{folioUnico}/comprobante")
    public ComprobanteResponse obtenerComprobantePorFolio(@PathVariable String folioUnico) {
        return pagoService.obtenerComprobantePorFolio(folioUnico);
    }
}
