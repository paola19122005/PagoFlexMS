package com.pagoflex.usuarios.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Cambio de rol desde la administracion (Agente). Para EJECUTIVO se exige
 * empresaId; para AGENTE se limpian los datos de empresa.
 */
public class UsuarioRolRequest {

    @NotBlank
    @Size(max = 20)
    private String rol;

    private Long empresaId;

    @Size(max = 120)
    private String empresa;

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
}
