package com.pagoflex.compromisos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CompromisoActualizacionPagoRequest {

    @NotBlank
    @Size(max = 30)
    private String estado;

    @Positive
    private Short numeroCuota;

    @NotNull
    private LocalDate fechaVencimiento;

    @NotNull
    @Min(0)
    private Integer recargoAplicado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Short getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(Short numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Integer getRecargoAplicado() {
        return recargoAplicado;
    }

    public void setRecargoAplicado(Integer recargoAplicado) {
        this.recargoAplicado = recargoAplicado;
    }
}
