package com.pagoflex.compromisos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CompromisoActualizacionRequest {

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
}
