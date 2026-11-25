package com.pagoflex.compromisos.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AnulacionRequest {

    @NotNull
    @Positive
    private Integer idEjecutivo;

    @NotBlank
    @Size(max = 255)
    private String motivo;

    public Integer getIdEjecutivo() {
        return idEjecutivo;
    }

    public void setIdEjecutivo(Integer idEjecutivo) {
        this.idEjecutivo = idEjecutivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
