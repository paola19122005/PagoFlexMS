package com.pagoflex.reportes.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ReporteEstadoRequest {

    @NotBlank
    private String estado;

    public ReporteEstadoRequest() {
    }

    public ReporteEstadoRequest(String estado) {
        this.estado = estado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
