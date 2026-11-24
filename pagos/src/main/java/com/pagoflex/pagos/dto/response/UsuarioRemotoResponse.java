package com.pagoflex.pagos.dto.response;

/**
 * Vista reducida de la respuesta de {@code usuarios}. Se usa para verificar
 * que el usuario exista y se encuentre habilitado antes de registrar el pago.
 */
public class UsuarioRemotoResponse {

    private Long id;
    private String nombre;
    private String email;
    private String rol;
    private Boolean activo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
