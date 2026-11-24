package com.pagoflex.pagos.mapper;

import com.pagoflex.pagos.dto.request.ComprobanteRegistroRequest;
import com.pagoflex.pagos.dto.request.PagoRegistroRequest;
import com.pagoflex.pagos.dto.response.ComprobanteResponse;
import com.pagoflex.pagos.dto.response.PagoResponse;
import com.pagoflex.pagos.entity.Comprobante;
import com.pagoflex.pagos.entity.Pago;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class PagoMapper {

    private static final String URI_COMPROBANTE_POR_DEFECTO = "pagoflex://comprobantes/";

    public Pago toEntity(PagoRegistroRequest request,
                         String folioUnico,
                         String numeroComprobante,
                         LocalDateTime fechaPago,
                         Integer cuotaPagada,
                         LocalDate fechaVencimientoCuota) {
        Pago pago = new Pago();
        pago.setFolioUnico(folioUnico);
        pago.setNumeroComprobante(numeroComprobante);
        pago.setIdUsuario(request.getUsuarioId());
        pago.setIdCompromiso(request.getCompromisoId());
        pago.setIdEmpresa(request.getEmpresaId());
        pago.setMontoPagado(request.getMontoPagado());
        pago.setCuotaPagada(cuotaPagada);
        pago.setMetodoPago(request.getMetodoPago().trim());
        pago.setFechaPago(fechaPago);
        pago.setFechaVencimientoCuota(fechaVencimientoCuota);
        pago.setCanal(request.getCanal() == null || request.getCanal().isBlank()
                ? "App"
                : request.getCanal().trim());
        pago.setFotoUri(request.getFotoUri());

        Comprobante comprobante = new Comprobante();
        ComprobanteRegistroRequest comprobanteRequest = request.getComprobante();
        String comprobanteUri = comprobanteRequest != null
                ? comprobanteRequest.getUri()
                : null;
        if ((comprobanteUri == null || comprobanteUri.isBlank())
                && request.getComprobanteUri() != null
                && !request.getComprobanteUri().isBlank()) {
            comprobanteUri = request.getComprobanteUri();
        }
        comprobante.setUri(comprobanteUri != null && !comprobanteUri.isBlank()
                ? comprobanteUri.trim()
                : URI_COMPROBANTE_POR_DEFECTO + folioUnico);

        LocalDateTime comprobanteFecha = comprobanteRequest != null
                ? comprobanteRequest.getFecha()
                : null;
        if (comprobanteFecha == null) {
            comprobanteFecha = request.getComprobanteFecha();
        }
        comprobante.setFecha(comprobanteFecha != null ? comprobanteFecha : fechaPago);
        comprobante.setPago(pago);
        pago.setComprobante(comprobante);

        return pago;
    }

    public PagoResponse toResponse(Pago pago) {
        PagoResponse response = new PagoResponse();
        response.setId(pago.getId());
        response.setFolioUnico(pago.getFolioUnico());
        response.setNumeroComprobante(pago.getNumeroComprobante());
        response.setUsuarioId(pago.getIdUsuario());
        response.setCompromisoId(pago.getIdCompromiso());
        response.setEmpresaId(pago.getIdEmpresa());
        response.setMontoPagado(pago.getMontoPagado());
        response.setCuotaPagada(pago.getCuotaPagada());
        response.setMetodoPago(pago.getMetodoPago());
        response.setFechaPago(pago.getFechaPago());
        response.setFechaVencimientoCuota(pago.getFechaVencimientoCuota());
        response.setCanal(pago.getCanal());
        response.setFotoUri(pago.getFotoUri());
        if (pago.getComprobante() != null) {
            response.setComprobante(toResponse(pago.getComprobante()));
        }
        return response;
    }

    public ComprobanteResponse toResponse(Comprobante comprobante) {
        ComprobanteResponse response = new ComprobanteResponse();
        response.setId(comprobante.getId());
        response.setIdPago(comprobante.getPago() != null ? comprobante.getPago().getId() : null);
        response.setUri(comprobante.getUri());
        response.setFecha(comprobante.getFecha());
        return response;
    }
}
