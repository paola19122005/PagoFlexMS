package com.pagoflex.reportes.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pagoflex.reportes.dto.request.ReporteEstadoRequest;
import com.pagoflex.reportes.dto.request.ReporteMensajeRequest;
import com.pagoflex.reportes.dto.request.ReporteRegistroRequest;
import com.pagoflex.reportes.dto.response.ReporteMensajeResponse;
import com.pagoflex.reportes.dto.response.ReporteResponse;
import com.pagoflex.reportes.service.ReporteMensajeService;
import com.pagoflex.reportes.service.ReporteService;

@ExtendWith(MockitoExtension.class)
class ReporteControllerTest {

    @Mock
    private ReporteService reporteService;

    @Mock
    private ReporteMensajeService mensajeService;

    @InjectMocks
    private ReporteController controller;

    @Test
    void crear() {

        when(reporteService.crear(any()))
                .thenReturn(new ReporteResponse());

        assertEquals(
                201,
                controller.crear(
                        new ReporteRegistroRequest()
                ).getStatusCode().value()
        );
    }

    @Test
    void listarTodos() {

        when(reporteService.listarTodos())
                .thenReturn(
                        List.of(new ReporteResponse())
                );

        assertEquals(
                1,
                controller.listarTodos().size()
        );
    }

    @Test
    void obtenerPorId() {

        when(reporteService.obtenerPorId(1L))
                .thenReturn(new ReporteResponse());

        controller.obtenerPorId(1L);
    }

    @Test
    void listarPorUsuario() {

        when(reporteService.listarPorUsuario(1L))
                .thenReturn(
                        List.of(new ReporteResponse())
                );

        assertEquals(
                1,
                controller.listarPorUsuario(1L).size()
        );
    }

    @Test
    void actualizarEstado() {

        when(
                reporteService.actualizarEstado(
                        eq(1L),
                        any()
                )
        ).thenReturn(new ReporteResponse());

        controller.actualizarEstado(
                1L,
                new ReporteEstadoRequest()
        );
    }

    @Test
    void listarMensajes() {

        when(mensajeService.listarPorReporte(1L))
                .thenReturn(
                        List.of(
                                new ReporteMensajeResponse()
                        )
                );

        assertEquals(
                1,
                controller.listarMensajes(1L).size()
        );
    }

    @Test
    void registrarMensaje() {

        when(
                mensajeService.registrar(
                        eq(1L),
                        any()
                )
        ).thenReturn(
                new ReporteMensajeResponse()
        );

        assertEquals(
                201,
                controller.registrarMensaje(
                        1L,
                        new ReporteMensajeRequest()
                ).getStatusCode().value()
        );
    }
}
