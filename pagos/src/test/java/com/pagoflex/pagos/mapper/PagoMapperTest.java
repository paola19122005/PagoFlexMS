package com.pagoflex.pagos.mapper;

import com.pagoflex.pagos.dto.request.ComprobanteRegistroRequest;
import com.pagoflex.pagos.dto.request.PagoRegistroRequest;
import com.pagoflex.pagos.dto.response.PagoResponse;
import com.pagoflex.pagos.entity.Comprobante;
import com.pagoflex.pagos.entity.Pago;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class PagoMapperTest {

    private final PagoMapper mapper = new PagoMapper();

    private PagoRegistroRequest request() {
        PagoRegistroRequest request = new PagoRegistroRequest();
        request.setUsuarioId(7L);
        request.setCompromisoId(21L);
        request.setEmpresaId(1009L);
        request.setMontoPagado(15000);
        request.setMetodoPago("Pago simulado");
        request.setCanal("App");
        return request;
    }

    @Test
    void toEntityMapeaCamposYCreaComprobantePorDefecto() {
        PagoRegistroRequest request = request();
        LocalDateTime fechaPago = LocalDateTime.of(2026, 3, 5, 10, 30);
        LocalDate vencimiento = LocalDate.of(2026, 3, 10);

        Pago pago = mapper.toEntity(request, "PF-TEST-1", "CPR-TEST-1", fechaPago, 2, vencimiento);

        assertEquals("PF-TEST-1", pago.getFolioUnico());
        assertEquals("CPR-TEST-1", pago.getNumeroComprobante());
        assertEquals(7L, pago.getIdUsuario());
        assertEquals(21L, pago.getIdCompromiso());
        assertEquals(1009L, pago.getIdEmpresa());
        assertEquals(15000, pago.getMontoPagado());
        assertEquals(2, pago.getCuotaPagada());
        assertEquals("Pago simulado", pago.getMetodoPago());
        assertEquals(fechaPago, pago.getFechaPago());
        assertEquals(vencimiento, pago.getFechaVencimientoCuota());
        assertEquals("App", pago.getCanal());

        Comprobante comprobante = pago.getComprobante();
        assertNotNull(comprobante);
        assertEquals("pagoflex://comprobantes/PF-TEST-1", comprobante.getUri());
        assertEquals(fechaPago, comprobante.getFecha());
        assertSame(pago, comprobante.getPago());
    }

    @Test
    void toEntityUsaComprobanteSolicitadoYValoresPorDefecto() {
        PagoRegistroRequest request = request();
        request.setCanal("  ");
        ComprobanteRegistroRequest comprobanteRequest = new ComprobanteRegistroRequest();
        comprobanteRequest.setUri("file:///comprobantes/1.pdf");
        comprobanteRequest.setFecha(LocalDateTime.of(2026, 3, 6, 8, 0));
        request.setComprobante(comprobanteRequest);

        Pago pago = mapper.toEntity(request, "PF-TEST-2", "CPR-TEST-2",
                LocalDateTime.of(2026, 3, 5, 10, 30), null, null);

        assertEquals("App", pago.getCanal());
        assertEquals("file:///comprobantes/1.pdf", pago.getComprobante().getUri());
        assertEquals(LocalDateTime.of(2026, 3, 6, 8, 0), pago.getComprobante().getFecha());
        assertNull(pago.getCuotaPagada());
        assertNull(pago.getFechaVencimientoCuota());
    }

    @Test
    void toEntityUsaComprobantePlanoCuandoNoHayObjeto() {
        PagoRegistroRequest request = request();
        request.setComprobanteUri("file:///comprobantes/plano.pdf");
        request.setComprobanteFecha(LocalDateTime.of(2026, 3, 7, 9, 15));

        Pago pago = mapper.toEntity(request, "PF-TEST-3", "CPR-TEST-3",
                LocalDateTime.of(2026, 3, 5, 10, 30), 1, null);

        assertEquals("file:///comprobantes/plano.pdf", pago.getComprobante().getUri());
        assertEquals(LocalDateTime.of(2026, 3, 7, 9, 15), pago.getComprobante().getFecha());
    }

    @Test
    void toResponseIncluyeComprobante() {
        Pago pago = new Pago();
        pago.setId(1L);
        pago.setFolioUnico("PF-1");
        pago.setNumeroComprobante("CPR-1");
        pago.setIdUsuario(7L);
        pago.setIdCompromiso(21L);
        pago.setIdEmpresa(1009L);
        pago.setMontoPagado(15000);
        pago.setMetodoPago("Pago simulado");
        pago.setFechaPago(LocalDateTime.of(2026, 3, 5, 10, 30));

        Comprobante comprobante = new Comprobante();
        comprobante.setId(70L);
        comprobante.setPago(pago);
        comprobante.setUri("pagoflex://comprobantes/PF-1");
        comprobante.setFecha(LocalDateTime.of(2026, 3, 5, 10, 30));
        pago.setComprobante(comprobante);

        PagoResponse response = mapper.toResponse(pago);

        assertEquals(1L, response.getId());
        assertEquals("PF-1", response.getFolioUnico());
        assertNotNull(response.getComprobante());
        assertEquals(70L, response.getComprobante().getId());
        assertEquals(1L, response.getComprobante().getIdPago());
    }
}
