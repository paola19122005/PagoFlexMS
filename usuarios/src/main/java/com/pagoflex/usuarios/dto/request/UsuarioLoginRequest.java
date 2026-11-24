package com.pagoflex.usuarios.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Login por correo o codigo de usuario (la app Android permite ambos).
 */
public class UsuarioLoginRequest {

    @NotBlank
    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
