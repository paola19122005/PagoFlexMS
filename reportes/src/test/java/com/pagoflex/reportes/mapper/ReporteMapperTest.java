package com.pagoflex.reportes.mapper;

import com.pagoflex.reportes.dto.request.ReporteRegistroRequest;
import com.pagoflex.reportes.dto.response.ReporteResponse;
import com.pagoflex.reportes.entity.Reporte;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReporteMapperTest {

    @Test
    void toEntity() {

        ReporteRegistroRequest request =
                new ReporteRegistroRequest();

        request.setTipo("Problema con compromiso");
        request.setCompromiso("CPF-000123");
        request.setDescripcion("Pago aún pendiente");
        request.setFecha("22-11-2026");
        request.setIdUsuario(5L);

        ReporteMapper mapper = new ReporteMapper();

        Reporte reporte = mapper.toEntity(request);

        assertEquals(
                "Problema con compromiso",
                reporte.getTipo()
        );

        assertEquals(
                "CPF-000123",
                reporte.getCompromiso()
        );

        assertEquals(
                "Pago aún pendiente",
                reporte.getDescripcion()
        );

        assertEquals(
                "22-11-2026",
                reporte.getFecha()
        );

        assertEquals(
                5L,
                reporte.getIdUsuario()
        );

        assertEquals(
                "PENDIENTE",
                reporte.getEstado()
        );
    }

    @Test
    void toResponse() {

        Reporte reporte = new Reporte();

        reporte.setId(1L);
        reporte.setTipo("Problema");
        reporte.setCompromiso("CPF-001");
        reporte.setDescripcion("Descripción");
        reporte.setFecha("22-11-2026");
        reporte.setIdUsuario(5L);
        reporte.setEstado("EN_PROCESO");

        ReporteMapper mapper = new ReporteMapper();

        ReporteResponse response =
                mapper.toResponse(reporte);

        assertEquals(1L, response.getId());
        assertEquals("Problema", response.getTipo());
        assertEquals("CPF-001", response.getCompromiso());
        assertEquals("Descripción", response.getDescripcion());
        assertEquals("22-11-2026", response.getFecha());
        assertEquals(5L, response.getIdUsuario());
        assertEquals("EN_PROCESO", response.getEstado());
    }
}