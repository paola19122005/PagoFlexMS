package com.pagoflex.pagos.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PagoRegistroRequest {

    @NotNull
    @Positive
    private Long usuarioId;

    @NotNull
    @Positive
    private Long compromisoId;

    @NotNull
    @Positive
    private Long empresaId;

    @NotNull
    @Positive
    private Integer montoPagado;

    @Positive
    private Integer cuotaPagada;

    @NotBlank
    @Size(max = 50)
    private String metodoPago;

    private LocalDateTime fechaPago;

    private LocalDate fechaVencimientoCuota;

    @Size(max = 40)
    private String folioUnico;

    @Size(max = 30)
    private String numeroComprobante;

    @Size(max = 30)
    private String canal;

    @Size(max = 500)
    private String fotoUri;

    /**
     * Alternativa plana al objeto {@link ComprobanteRegistroRequest}. Facilita
     * la integracion desde clientes que manejan el comprobante como un campo
     * independiente.
     */
    @Size(max = 500)
    private String comprobanteUri;

    private LocalDateTime comprobanteFecha;

    @Valid
    private ComprobanteRegistroRequest comprobante;

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

    public String getComprobanteUri() {
        return comprobanteUri;
    }

    public void setComprobanteUri(String comprobanteUri) {
        this.comprobanteUri = comprobanteUri;
    }

    public LocalDateTime getComprobanteFecha() {
        return comprobanteFecha;
    }

    public void setComprobanteFecha(LocalDateTime comprobanteFecha) {
        this.comprobanteFecha = comprobanteFecha;
    }

    public ComprobanteRegistroRequest getComprobante() {
        return comprobante;
    }

    public void setComprobante(ComprobanteRegistroRequest comprobante) {
        this.comprobante = comprobante;
    }
}
