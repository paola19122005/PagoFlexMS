package com.pagoflex.usuarios.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioCambioContrasenaRequest {
    @NotBlank
    private String actual;
    @NotBlank
    @Size(min = 8)
    private String nueva;

    public String getActual() { return actual; }
    public void setActual(String actual) { this.actual = actual; }
    public String getNueva() { return nueva; }
    public void setNueva(String nueva) { this.nueva = nueva; }
}