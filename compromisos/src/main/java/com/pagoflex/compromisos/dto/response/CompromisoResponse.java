package com.pagoflex.compromisos.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CompromisoResponse {

    private Integer idCompromiso;
    private String folio;
    private Integer idUsuarioFinal;
    private Integer idEmpresa;
    private String concepto;
    private Short numeroCuota;
    private Short totalCuotas;
    private Integer monto;
    private LocalDate fechaVencimiento;
    private String estado;
    private Integer recargoAplicado;
    private LocalDateTime fechaCambioEstado;
    private Integer idEjecutivoRegistro;
    private LocalDateTime fechaRegistro;

    private String sistemaOrigen;

    private String idInstanciaOrigen;

    private Long idCompromisoOrigen;

    public Integer getIdCompromiso() {
        return idCompromiso;
    }

    public void setIdCompromiso(Integer idCompromiso) {
        this.idCompromiso = idCompromiso;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public Integer getIdUsuarioFinal() {
        return idUsuarioFinal;
    }

    public void setIdUsuarioFinal(Integer idUsuarioFinal) {
        this.idUsuarioFinal = idUsuarioFinal;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
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

    public LocalDateTime getFechaCambioEstado() {
        return fechaCambioEstado;
    }

    public void setFechaCambioEstado(LocalDateTime fechaCambioEstado) {
        this.fechaCambioEstado = fechaCambioEstado;
    }

    public Integer getIdEjecutivoRegistro() {
        return idEjecutivoRegistro;
    }

    public void setIdEjecutivoRegistro(Integer idEjecutivoRegistro) {
        this.idEjecutivoRegistro = idEjecutivoRegistro;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getSistemaOrigen() {
        return sistemaOrigen;
    }

    public void setSistemaOrigen(String sistemaOrigen) {
        this.sistemaOrigen = sistemaOrigen;
    }

    public String getIdInstanciaOrigen() {
        return idInstanciaOrigen;
    }

    public void setIdInstanciaOrigen(String idInstanciaOrigen) {
        this.idInstanciaOrigen = idInstanciaOrigen;
    }

    public Long getIdCompromisoOrigen() {
        return idCompromisoOrigen;
    }

    public void setIdCompromisoOrigen(Long idCompromisoOrigen) {
        this.idCompromisoOrigen = idCompromisoOrigen;
    }
}
