package com.pagoflex.usuarios.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UsuarioResetContrasenaRequest {
    @NotBlank
    private String email;
    @NotBlank
    @Size(min = 8)
    private String nuevaPassword;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getNuevaPassword() { return nuevaPassword; }
    public void setNuevaPassword(String nuevaPassword) { this.nuevaPassword = nuevaPassword; }
}