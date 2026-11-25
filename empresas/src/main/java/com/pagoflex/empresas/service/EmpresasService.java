package com.pagoflex.empresas.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pagoflex.empresas.dto.request.EmpresasRegistroRequest;
import com.pagoflex.empresas.dto.response.EmpresasResponse;
import com.pagoflex.empresas.entity.Empresas;
import com.pagoflex.empresas.exception.EmpresaDuplicadaException;
import com.pagoflex.empresas.exception.EmpresaNoEncontradaException;
import com.pagoflex.empresas.mapper.EmpresasMapper;
import com.pagoflex.empresas.repository.EmpresasRepository;

@Service
public class EmpresasService {

    private final EmpresasRepository empresasRepository;
    private final EmpresasMapper empresasMapper;

    public EmpresasService(EmpresasRepository empresasRepository,
                            EmpresasMapper empresasMapper) {
        this.empresasRepository = empresasRepository;
        this.empresasMapper = empresasMapper;
    }

    @Transactional
    public EmpresasResponse crear(EmpresasRegistroRequest request) {

        if (empresasRepository.existsByCodigo(request.getCodigo())) {
            throw new EmpresaDuplicadaException(
                    "Ya existe una empresa con el código: " + request.getCodigo()
                );
        }

        if (empresasRepository.existsByRut(request.getRut())) {
            throw new EmpresaDuplicadaException(
                    "Ya existe una empresa con el RUT: " + request.getRut()
            );
        }

        Empresas empresas = empresasMapper.toEntity(request);

        Empresas guardada = empresasRepository.save(empresas);

        return empresasMapper.toResponse(guardada);
    }

    @Transactional(readOnly = true)
    public List<EmpresasResponse> listarTodos() {

        return empresasRepository.findAll()
                .stream()
                .map(empresasMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EmpresasResponse obtenerPorId(Long id) {

        Empresas empresas = empresasRepository.findById(id)
                .orElseThrow(() ->
                        new EmpresaNoEncontradaException(
                                "Empresa no encontrada: " + id
                        )
            );
        
        return empresasMapper.toResponse(empresas);
    }

    @Transactional(readOnly = true)
    public EmpresasResponse obtenerPorCodigo(String codigo) {

        Empresas empresas = empresasRepository.findByCodigo(codigo)
                .orElseThrow(() ->
                        new EmpresaNoEncontradaException(
                                "Empresa no encontrada con código: " + codigo
                        )
                );
        
        return empresasMapper.toResponse(empresas);
    }

    @Transactional(readOnly = true)
    public EmpresasResponse obtenerPorRut(String rut) {

        Empresas empresas = empresasRepository.findByRut(rut)
                .orElseThrow(() ->
                        new EmpresaNoEncontradaException(
                                "Empresa no encontrada con RUT: " + rut
                        )
                );
        
        return empresasMapper.toResponse(empresas);
    }

    @Transactional
    public EmpresasResponse actualizar(
            Long id,    
            EmpresasRegistroRequest request) {
        
        Empresas empresas = empresasRepository.findById(id)
                .orElseThrow(() ->
                        new EmpresaNoEncontradaException(
                                "Empresa no encontrada: " + id
                        )
                );

        empresasRepository.findByCodigo(request.getCodigo())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> {
                    throw new EmpresaDuplicadaException(
                            "Ya existe una empresa con el código: "
                                    + request.getCodigo()
                    );
                });

        empresasRepository.findByRut(request.getRut())
                .filter(e -> !e.getId().equals(id))
                .ifPresent(e -> {
                    throw new EmpresaDuplicadaException(
                            "Ya existe una empresa con el RUT: "
                                    + request.getRut()
                    );
                });

        empresasMapper.actualizarEntity(empresas, request);

        Empresas actualizada = empresasRepository.save(empresas);

        return empresasMapper.toResponse(actualizada);
    }

    @Transactional
    public void eliminar(Long id) {

        Empresas empresas = empresasRepository.findById(id)
                .orElseThrow(() ->
                        new EmpresaNoEncontradaException(
                                "Empresa no encontrada: " + id
                        )
                );

        empresasRepository.delete(empresas);
    }
}
