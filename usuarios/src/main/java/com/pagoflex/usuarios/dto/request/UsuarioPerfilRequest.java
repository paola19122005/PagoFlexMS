package com.pagoflex.usuarios.dto.request;

import jakarta.validation.constraints.Size;

/**
 * Actualizacion parcial del perfil: solo se modifican los campos informados.
 */
public class UsuarioPerfilRequest {

    @Size(min = 3, max = 120)
    private String nombre;

    @Size(max = 150)
    private String email;

    @Size(max = 30)
    private String telefono;

    @Size(max = 120)
    private String preguntaSeguridad;

    @Size(max = 120)
    private String respuestaSeguridad;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getPreguntaSeguridad() {
        return preguntaSeguridad;
    }

    public void setPreguntaSeguridad(String preguntaSeguridad) {
        this.preguntaSeguridad = preguntaSeguridad;
    }

    public String getRespuestaSeguridad() {
        return respuestaSeguridad;
    }

    public void setRespuestaSeguridad(String respuestaSeguridad) {
        this.respuestaSeguridad = respuestaSeguridad;
    }
}
