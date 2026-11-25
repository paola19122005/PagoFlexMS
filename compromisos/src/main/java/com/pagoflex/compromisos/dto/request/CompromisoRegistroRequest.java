package com.pagoflex.compromisos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CompromisoRegistroRequest {

    @NotNull
    private Integer idUsuarioFinal;

    @NotNull
    private Integer idEmpresa;

    @NotBlank
    @Size(max = 120)
    private String concepto;

    @Positive
    private Short numeroCuota;

    @Positive
    private Short totalCuotas;

    @NotNull
    @Positive
    private Integer monto;

    @NotNull
    private LocalDate fechaVencimiento;

    private Integer idEjecutivoRegistro;

    @Size(max = 30)
    private String sistemaOrigen;

    @Size(max = 36)
    private String idInstanciaOrigen;

    @Positive
    private Long idCompromisoOrigen;

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

    public Integer getIdEjecutivoRegistro() {
        return idEjecutivoRegistro;
    }

    public void setIdEjecutivoRegistro(Integer idEjecutivoRegistro) {
        this.idEjecutivoRegistro = idEjecutivoRegistro;
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
