package com.pagoflex.compromisos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CompromisoActualizacionVencimientoRequest {

    @NotBlank
    @Size(max = 30)
    private String estado;

    @NotNull
    @Min(0)
    private Integer recargoAplicado;

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
