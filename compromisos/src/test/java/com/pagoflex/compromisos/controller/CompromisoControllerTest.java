
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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

        when(compromisoRegistroService.registrar(any()))
                .thenReturn(new CompromisoRegistroResultado(true, response));

        assertEquals(201, controller.registrar(new CompromisoRegistroRequest()).getStatusCode().value());
    }

    @Test
    void registrarCuandoNoEsCreado() {
        CompromisoResponse response = new CompromisoResponse();

        when(compromisoRegistroService.registrar(any()))
                .thenReturn(new CompromisoRegistroResultado(false, response));

        assertEquals(200, controller.registrar(new CompromisoRegistroRequest()).getStatusCode().value());
    }

    @Test
    void obtenerPorId() {
        when(compromisoConsultaService.obtenerPorId(1)).thenReturn(new CompromisoResponse());
        controller.obtenerPorId(1);
    }

    @Test
    void obtenerPorFolio() {
        CompromisoResponse response = new CompromisoResponse();

        when(compromisoConsultaService.obtenerPorFolio("FOL-001"))
                .thenReturn(response);

        assertSame(response, controller.obtenerPorFolio("FOL-001"));
        verify(compromisoConsultaService).obtenerPorFolio("FOL-001");
    }

    @Test
    void listarPorUsuario() {
        List<CompromisoResponse> response = List.of(new CompromisoResponse());

        when(compromisoConsultaService.listarPorUsuario(1))
                .thenReturn(response);

        assertSame(response, controller.listarPorUsuario(1));
        verify(compromisoConsultaService).listarPorUsuario(1);
    }

    @Test
    void listarPorEmpresa() {
        List<CompromisoResponse> response = List.of(new CompromisoResponse());

        when(compromisoConsultaService.listarPorEmpresa(2))
                .thenReturn(response);

        assertSame(response, controller.listarPorEmpresa(2));
        verify(compromisoConsultaService).listarPorEmpresa(2);
    }

    @Test
    void listarPorUsuarioYEmpresa() {
        List<CompromisoResponse> response = List.of(new CompromisoResponse());

        when(compromisoConsultaService.listarPorUsuarioYEmpresa(1, 2))
                .thenReturn(response);

        assertSame(response, controller.listarPorUsuarioYEmpresa(1, 2));
        verify(compromisoConsultaService).listarPorUsuarioYEmpresa(1, 2);
    }

    @Test
    void listarPorEstado() {
        List<CompromisoResponse> response = List.of(new CompromisoResponse());

        when(compromisoConsultaService.listarPorEstado("PENDIENTE"))
                .thenReturn(response);

        assertSame(response, controller.listarPorEstado("PENDIENTE"));
        verify(compromisoConsultaService).listarPorEstado("PENDIENTE");
    }

    @Test
    void listarTodos() {
        when(compromisoConsultaService.listarTodos()).thenReturn(List.of(new CompromisoResponse()));
        controller.listarTodos();
    }

    @Test
    void actualizar() {
        when(compromisoActualizacionService.actualizar(eq(1), any()))
                .thenReturn(new CompromisoResponse());
        controller.actualizar(1, new CompromisoActualizacionRequest());
    }

    @Test
    void anular() {
        when(compromisoAnulacionService.anular(eq(1), any()))
                .thenReturn(new AnulacionResponse());
        controller.anular(1, new AnulacionRequest());
    }

    @Test
    void pago() {
        when(compromisoActualizacionPagoService.actualizarPorPago(eq(1), any()))
                .thenReturn(new CompromisoResponse());
        controller.actualizarPorPago(1, new CompromisoActualizacionPagoRequest());
    }

    @Test
    void vencimiento() {
        when(compromisoActualizacionVencimientoService.actualizarPorVencimiento(eq(1), any()))
                .thenReturn(new CompromisoResponse());
        controller.actualizarPorVencimiento(1, new CompromisoActualizacionVencimientoRequest());
    }

    @Test
    void revision() {
        when(compromisoActualizacionRevisionService.actualizarRevision(1))
                .thenReturn(new CompromisoResponse());
        controller.actualizarRevision(1);
    }
}
