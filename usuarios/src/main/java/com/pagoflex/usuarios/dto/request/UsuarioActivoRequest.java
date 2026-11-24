package com.pagoflex.usuarios.dto.request;

import jakarta.validation.constraints.NotNull;

/**
 * Habilita o deshabilita una cuenta (PATCH /api/usuarios/{id}/activo).
 */
public class UsuarioActivoRequest {

    @NotNull
    private Boolean activo;

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
