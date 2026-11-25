package com.pagoflex.pagos.controller;

import com.pagoflex.pagos.dto.request.PagoRegistroRequest;
import com.pagoflex.pagos.dto.response.ComprobanteResponse;
import com.pagoflex.pagos.dto.response.PagoResumenResponse;
import com.pagoflex.pagos.dto.response.PagoResponse;
import com.pagoflex.pagos.service.PagoService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagoControllerTest {

    @Mock
    private PagoService pagoService;

    @InjectMocks
    private PagoController controller;

    @Test
    void registrarDevuelveCreated() {
        PagoResponse response = new PagoResponse();
        when(pagoService.registrar(any())).thenReturn(response);

        var resultado = controller.registrar(new PagoRegistroRequest());

        assertEquals(201, resultado.getStatusCode().value());
        assertSame(response, resultado.getBody());
    }

    @Test
    void obtenerPorIdDelegacion() {
        PagoResponse response = new PagoResponse();
        when(pagoService.obtenerPorId(1L)).thenReturn(response);

        assertSame(response, controller.obtenerPorId(1L));
    }

    @Test
    void obtenerPorFolioDelegacion() {
        PagoResponse response = new PagoResponse();
        when(pagoService.obtenerPorFolioUnico("PF-1")).thenReturn(response);

        assertSame(response, controller.obtenerPorFolioUnico("PF-1"));
    }

    @Test
    void listarPorUsuarioDevuelveLista() {
        when(pagoService.listarPorUsuario(7L)).thenReturn(List.of(new PagoResponse()));

        assertEquals(1, controller.listarPorUsuario(7L).size());
    }

    @Test
    void listarPorCompromisoDevuelveLista() {
        when(pagoService.listarPorCompromiso(21L)).thenReturn(List.of(new PagoResponse()));

        assertEquals(1, controller.listarPorCompromiso(21L).size());
    }

    @Test
    void listarPorEmpresaDevuelveLista() {
        when(pagoService.listarPorEmpresa(1009L)).thenReturn(List.of(new PagoResponse()));

        assertEquals(1, controller.listarPorEmpresa(1009L).size());
    }

    @Test
    void resumenPorEmpresaDelegacion() {
        PagoResumenResponse resumen = new PagoResumenResponse();
        when(pagoService.resumenPorEmpresa(1009L)).thenReturn(resumen);

        assertSame(resumen, controller.resumenPorEmpresa(1009L));
    }

    @Test
    void listarTodosDevuelveLista() {
        when(pagoService.listarTodos()).thenReturn(List.of(new PagoResponse()));

        assertEquals(1, controller.listarTodos().size());
    }

    @Test
    void obtenerComprobantePorPagoDelegacion() {
        ComprobanteResponse response = new ComprobanteResponse();
        when(pagoService.obtenerComprobantePorPago(1L)).thenReturn(response);

        assertSame(response, controller.obtenerComprobantePorPago(1L));
    }

    @Test
    void obtenerComprobantePorFolioDelegacion() {
        ComprobanteResponse response = new ComprobanteResponse();
        when(pagoService.obtenerComprobantePorFolio(eq("PF-1"))).thenReturn(response);

        assertSame(response, controller.obtenerComprobantePorFolio("PF-1"));
    }
}
