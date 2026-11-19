package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoRegistroRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.exception.OrigenDatosInconsistentesException;
import com.pagoflex.compromisos.exception.ViolacionIntegridadException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompromisoRegistroService {

    private final CompromisoRegistroTransaccionalService transaccionalService;
    private final CompromisoOrigenRecuperacionService recuperacionService;
    private final CompromisoMapper compromisoMapper;

    public CompromisoRegistroService(CompromisoRegistroTransaccionalService transaccionalService,
                                     CompromisoOrigenRecuperacionService recuperacionService,
                                     CompromisoMapper compromisoMapper) {
        this.transaccionalService = transaccionalService;
        this.recuperacionService = recuperacionService;
        this.compromisoMapper = compromisoMapper;
    }

    public CompromisoRegistroResultado registrar(CompromisoRegistroRequest request) {
        validarIdentidadOrigen(request);

        boolean origenInformado = request.getSistemaOrigen() != null
                && request.getIdInstanciaOrigen() != null
                && request.getIdCompromisoOrigen() != null;

        if (origenInformado) {
            Optional<Compromiso> existente = recuperacionService.obtenerPorOrigen(
                    request.getSistemaOrigen(),
                    request.getIdInstanciaOrigen(),
                    request.getIdCompromisoOrigen());

            if (existente.isPresent()) {
                if (datosOriginalesIguales(request, existente.get())) {
                    return new CompromisoRegistroResultado(false, compromisoMapper.toResponse(existente.get()));
                }
                throw new OrigenDatosInconsistentesException(
                        "Ya existe un compromiso con la misma identidad de origen pero con datos diferentes");
            }
        }

        try {
            return transaccionalService.registrar(request);
        } catch (DataIntegrityViolationException ex) {
            String restriccion = obtenerRestriccion(ex);

            if ("uq_compromiso_origen".equalsIgnoreCase(restriccion) && origenInformado) {
                Optional<Compromiso> recuperado = recuperacionService.obtenerPorOrigen(
                        request.getSistemaOrigen(),
                        request.getIdInstanciaOrigen(),
                        request.getIdCompromisoOrigen());

                if (recuperado.isPresent()) {
                    if (datosOriginalesIguales(request, recuperado.get())) {
                        return new CompromisoRegistroResultado(false, compromisoMapper.toResponse(recuperado.get()));
                    }
                    throw new OrigenDatosInconsistentesException(
                            "Ya existe un compromiso con la misma identidad de origen pero con datos diferentes");
                }
                throw new CompromisoPersistenciaException("Error al guardar el compromiso", ex);
            }

            if ("uk_compromiso_unicidad".equalsIgnoreCase(restriccion)) {
                throw new ViolacionIntegridadException("COMPROMISO_DUPLICADO",
                        "Ya existe un compromiso con la misma combinación de usuario, empresa, concepto y cuota", ex);
            }

            throw new CompromisoPersistenciaException("Error al guardar el compromiso", ex);
        }
    }

    private boolean datosOriginalesIguales(CompromisoRegistroRequest request, Compromiso existente) {
        return java.util.Objects.equals(request.getIdUsuarioFinal(), existente.getIdUsuarioFinal())
                && java.util.Objects.equals(request.getIdEmpresa(), existente.getIdEmpresa())
                && java.util.Objects.equals(request.getConcepto(), existente.getConcepto())
                && java.util.Objects.equals(request.getNumeroCuota(), existente.getNumeroCuota())
                && java.util.Objects.equals(request.getTotalCuotas(), existente.getTotalCuotas())
                && java.util.Objects.equals(request.getMonto(), existente.getMonto())
                && java.util.Objects.equals(request.getFechaVencimiento(), existente.getFechaVencimiento())
                && java.util.Objects.equals(request.getIdEjecutivoRegistro(), existente.getIdEjecutivoRegistro());
    }

    private void validarIdentidadOrigen(CompromisoRegistroRequest request) {
        boolean algunoInformado = request.getSistemaOrigen() != null
                || request.getIdInstanciaOrigen() != null
                || request.getIdCompromisoOrigen() != null;

        if (!algunoInformado) {
            return;
        }

        if (request.getSistemaOrigen() == null
                || request.getIdInstanciaOrigen() == null
                || request.getIdCompromisoOrigen() == null) {
            throw new IllegalArgumentException("Los campos de identidad de origen deben informarse juntos");
        }

        if (request.getSistemaOrigen().isBlank()) {
            throw new IllegalArgumentException("sistemaOrigen no puede estar vacío");
        }

        if (request.getSistemaOrigen().length() > 30) {
            throw new IllegalArgumentException("sistemaOrigen no puede superar 30 caracteres");
        }

        try {
            java.util.UUID.fromString(request.getIdInstanciaOrigen().trim());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("idInstanciaOrigen debe ser un UUID válido");
        }

        if (request.getIdInstanciaOrigen().trim().length() != 36) {
            throw new IllegalArgumentException("idInstanciaOrigen debe tener 36 caracteres");
        }

        if (request.getIdCompromisoOrigen() <= 0) {
            throw new IllegalArgumentException("idCompromisoOrigen debe ser mayor que cero");
        }
    }

    private String obtenerRestriccion(org.springframework.dao.DataIntegrityViolationException ex) {
        Throwable causa = ex;
        while (causa != null) {
            if (causa instanceof org.hibernate.exception.ConstraintViolationException hibernateEx) {
                return hibernateEx.getConstraintName();
            }
            causa = causa.getCause();
        }
        return null;
    }
}
