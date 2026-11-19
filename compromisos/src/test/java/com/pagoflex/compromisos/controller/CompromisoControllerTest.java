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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompromisoControllerTest {

    @Mock
    private CompromisoRegistroService compromisoRegistroService;
    @Mock
    private CompromisoConsultaService compromisoConsultaService;
    @Mock
    private CompromisoActualizacionService compromisoActualizacionService;
    @Mock
    private CompromisoAnulacionService compromisoAnulacionService;
    @Mock
    private CompromisoActualizacionPagoService compromisoActualizacionPagoService;
    @Mock
    private CompromisoActualizacionVencimientoService compromisoActualizacionVencimientoService;
    @Mock
    private CompromisoActualizacionRevisionService compromisoActualizacionRevisionService;

    @InjectMocks
    private CompromisoController controller;

    @Test
    void registrar() {
        CompromisoResponse response = new CompromisoResponse();
        when(compromisoRegistroService.registrar(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new CompromisoRegistroResultado(true, response));
        assertEquals(201, controller.registrar(new CompromisoRegistroRequest()).getStatusCode().value());
    }

    @Test
    void obtenerPorId() {
        when(compromisoConsultaService.obtenerPorId(1)).thenReturn(new CompromisoResponse());
        controller.obtenerPorId(1);
    }

    @Test
    void listarTodos() {
        when(compromisoConsultaService.listarTodos()).thenReturn(List.of(new CompromisoResponse()));
        controller.listarTodos();
    }

    @Test
    void actualizar() {
        when(compromisoActualizacionService.actualizar(org.mockito.ArgumentMatchers.eq(1), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new CompromisoResponse());
        controller.actualizar(1, new CompromisoActualizacionRequest());
    }

    @Test
    void anular() {
        when(compromisoAnulacionService.anular(org.mockito.ArgumentMatchers.eq(1), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new AnulacionResponse());
        controller.anular(1, new AnulacionRequest());
    }

    @Test
    void pago() {
        when(compromisoActualizacionPagoService.actualizarPorPago(org.mockito.ArgumentMatchers.eq(1), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new CompromisoResponse());
        controller.actualizarPorPago(1, new CompromisoActualizacionPagoRequest());
    }

    @Test
    void vencimiento() {
        when(compromisoActualizacionVencimientoService.actualizarPorVencimiento(org.mockito.ArgumentMatchers.eq(1), org.mockito.ArgumentMatchers.any()))
                .thenReturn(new CompromisoResponse());
        controller.actualizarPorVencimiento(1, new CompromisoActualizacionVencimientoRequest());
    }

    @Test
    void revision() {
        when(compromisoActualizacionRevisionService.actualizarRevision(1)).thenReturn(new CompromisoResponse());
        controller.actualizarRevision(1);
    }
}
