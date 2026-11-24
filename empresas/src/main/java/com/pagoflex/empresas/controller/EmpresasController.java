package com.pagoflex.empresas.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pagoflex.empresas.dto.request.EmpresasRegistroRequest;
import com.pagoflex.empresas.dto.response.EmpresasResponse;
import com.pagoflex.empresas.service.EmpresasService;

import jakarta.validation.Valid;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/empresas")
public class EmpresasController {

    private final EmpresasService empresasService;

    public EmpresasController(EmpresasService empresasService) {
        this.empresasService = empresasService;
    }

    @PostMapping
    public ResponseEntity<EmpresasResponse> crear(
            @Valid @RequestBody EmpresasRegistroRequest request) {

        EmpresasResponse response = empresasService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public List<EmpresasResponse> listarTodos() {
        return empresasService.listarTodos();
    }

    @GetMapping("/{id}")
    public EmpresasResponse obtenerPorId(
            @PathVariable Long id) {

        return empresasService.obtenerPorId(id);
    }

    @GetMapping("/codigo/{codigo}")
    public EmpresasResponse obtenerPorCodigo(
            @PathVariable String codigo) {

        return empresasService.obtenerPorCodigo(codigo);
    }

    @GetMapping("/rut/{rut}")
    public EmpresasResponse obtenerPorRut(
            @PathVariable String rut) {

        return empresasService.obtenerPorRut(rut);
    }

    @PutMapping("/{id}")
    public EmpresasResponse actualizar(
            @PathVariable Long id,    
            @Valid @RequestBody EmpresasRegistroRequest request) {


        return empresasService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        empresasService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}
