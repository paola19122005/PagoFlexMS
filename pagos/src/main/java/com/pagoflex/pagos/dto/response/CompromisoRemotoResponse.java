package com.pagoflex.pagos.dto.response;

import java.time.LocalDate;

/**
 * Vista reducida de la respuesta de {@code compromisos}. Se usa para validar
 * la cuota antes de registrar el pago y para construir la actualizacion.
 */
public class CompromisoRemotoResponse {

    private Long idCompromiso;
    private String folio;
    private Long idUsuarioFinal;
    private Long idEmpresa;
    private Short numeroCuota;
    private Short totalCuotas;
    private Integer monto;
    private LocalDate fechaVencimiento;
    private String estado;
    private Integer recargoAplicado;

    public Long getIdCompromiso() {
        return idCompromiso;
    }

    public void setIdCompromiso(Long idCompromiso) {
        this.idCompromiso = idCompromiso;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Long getIdUsuarioFinal() {
        return idUsuarioFinal;
    }

    public void setIdUsuarioFinal(Long idUsuarioFinal) {
        this.idUsuarioFinal = idUsuarioFinal;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Short getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(Short numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public Short getTotalCuotas() {
        return totalCuotas;
    }

    public void setTotalCuotas(Short totalCuotas) {
        this.totalCuotas = totalCuotas;
    }

    public Integer getMonto() {
        return monto;
    }

    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getRecargoAplicado() {
        return recargoAplicado;
    }

    public void setRecargoAplicado(Integer recargoAplicado) {
        this.recargoAplicado = recargoAplicado;
    }
}
