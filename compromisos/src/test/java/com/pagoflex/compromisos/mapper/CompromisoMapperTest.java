package com.pagoflex.compromisos.mapper;

import com.pagoflex.compromisos.dto.request.CompromisoRegistroRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CompromisoMapperTest {

    @Test
    void toEntityMapsAllFields() {
        CompromisoRegistroRequest request = new CompromisoRegistroRequest();
        request.setIdUsuarioFinal(1);
        request.setIdEmpresa(2);
        request.setConcepto("Concepto");
        request.setNumeroCuota((short) 3);
        request.setTotalCuotas((short) 4);
        request.setMonto(100);
        request.setFechaVencimiento(LocalDate.of(2026, 1, 1));
        request.setIdEjecutivoRegistro(5);
        request.setSistemaOrigen("ANDROID_ROOM");
        request.setIdInstanciaOrigen("11111111-2222-3333-4444-555555555555");
        request.setIdCompromisoOrigen(6L);

        Compromiso compromiso = new CompromisoMapper().toEntity(request);

        assertEquals(1, compromiso.getIdUsuarioFinal());
        assertEquals(2, compromiso.getIdEmpresa());
        assertEquals("Concepto", compromiso.getConcepto());
        assertEquals((short) 3, compromiso.getNumeroCuota());
        assertEquals((short) 4, compromiso.getTotalCuotas());
        assertEquals(100, compromiso.getMonto());
        assertEquals(LocalDate.of(2026, 1, 1), compromiso.getFechaVencimiento());
        assertEquals(5, compromiso.getIdEjecutivoRegistro());
        assertEquals("ANDROID_ROOM", compromiso.getSistemaOrigen());
        assertEquals("11111111-2222-3333-4444-555555555555", compromiso.getIdInstanciaOrigen());
        assertEquals(6L, compromiso.getIdCompromisoOrigen());
        assertEquals(0, compromiso.getRecargoAplicado());
    }

    @Test
    void toResponseMapsAllFields() {
        Compromiso compromiso = new Compromiso();
        compromiso.setIdCompromiso(10);
        compromiso.setFolio("CP-2026-0010");
        compromiso.setIdUsuarioFinal(1);
        compromiso.setIdEmpresa(2);
        compromiso.setConcepto("Concepto");
        compromiso.setNumeroCuota((short) 3);
        compromiso.setTotalCuotas((short) 4);
        compromiso.setMonto(100);
        compromiso.setFechaVencimiento(LocalDate.of(2026, 1, 1));
        compromiso.setRecargoAplicado(5);
        compromiso.setIdEjecutivoRegistro(6);
        compromiso.setSistemaOrigen("ANDROID_ROOM");
        compromiso.setIdInstanciaOrigen("11111111-2222-3333-4444-555555555555");
        compromiso.setIdCompromisoOrigen(7L);

        CompromisoResponse response = new CompromisoMapper().toResponse(compromiso);

        assertEquals(10, response.getIdCompromiso());
        assertEquals("CP-2026-0010", response.getFolio());
        assertEquals(1, response.getIdUsuarioFinal());
        assertEquals(2, response.getIdEmpresa());
        assertEquals("Concepto", response.getConcepto());
        assertEquals((short) 3, response.getNumeroCuota());
        assertEquals((short) 4, response.getTotalCuotas());
        assertEquals(100, response.getMonto());
        assertEquals(LocalDate.of(2026, 1, 1), response.getFechaVencimiento());
        assertEquals(5, response.getRecargoAplicado());
        assertEquals(6, response.getIdEjecutivoRegistro());
        assertEquals("ANDROID_ROOM", response.getSistemaOrigen());
        assertEquals("11111111-2222-3333-4444-555555555555", response.getIdInstanciaOrigen());
        assertEquals(7L, response.getIdCompromisoOrigen());
    }

    @Test
    void actualizarEntidadUpdatesFields() {
        Compromiso compromiso = new Compromiso();
        com.pagoflex.compromisos.dto.request.CompromisoActualizacionRequest request =
                new com.pagoflex.compromisos.dto.request.CompromisoActualizacionRequest();
        request.setConcepto("Nuevo");
        request.setNumeroCuota((short) 2);
        request.setTotalCuotas((short) 3);
        request.setMonto(200);
        request.setFechaVencimiento(LocalDate.of(2026, 2, 2));

        new CompromisoMapper().actualizarEntidad(compromiso, request);

        assertEquals("Nuevo", compromiso.getConcepto());
        assertEquals((short) 2, compromiso.getNumeroCuota());
        assertEquals((short) 3, compromiso.getTotalCuotas());
        assertEquals(200, compromiso.getMonto());
        assertEquals(LocalDate.of(2026, 2, 2), compromiso.getFechaVencimiento());
    }
}
