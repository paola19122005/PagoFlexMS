package com.pagoflex.pagos.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PagoResponse {

    private Long id;
    private String folioUnico;
    private String numeroComprobante;
    private Long usuarioId;
    private Long compromisoId;
    private Long empresaId;
    private Integer montoPagado;
    private Integer cuotaPagada;
    private String metodoPago;
    private LocalDateTime fechaPago;
    private LocalDate fechaVencimientoCuota;
    private String canal;
    private String fotoUri;
    private ComprobanteResponse comprobante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFolioUnico() {
        return folioUnico;
    }

    public void setFolioUnico(String folioUnico) {
        this.folioUnico = folioUnico;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        this.numeroComprobante = numeroComprobante;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getCompromisoId() {
        return compromisoId;
    }

    public void setCompromisoId(Long compromisoId) {
        this.compromisoId = compromisoId;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Integer getMontoPagado() {
        return montoPagado;
    }

    public void setMontoPagado(Integer montoPagado) {
        this.montoPagado = montoPagado;
    }

    public Integer getCuotaPagada() {
        return cuotaPagada;
    }

    public void setCuotaPagada(Integer cuotaPagada) {
        this.cuotaPagada = cuotaPagada;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public LocalDate getFechaVencimientoCuota() {
        return fechaVencimientoCuota;
    }

    public void setFechaVencimientoCuota(LocalDate fechaVencimientoCuota) {
        this.fechaVencimientoCuota = fechaVencimientoCuota;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    public ComprobanteResponse getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobanteResponse comprobante) {
        this.comprobante = comprobante;
    }
}
