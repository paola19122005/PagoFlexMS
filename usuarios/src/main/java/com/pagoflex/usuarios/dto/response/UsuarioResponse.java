package com.pagoflex.usuarios.dto.response;

/**
 * Respuesta publica del usuario. No expone passwordHash, salt ni la
 * respuesta de seguridad.
 */
public class UsuarioResponse {

    private Long id;
    private String codigo;
    private String nombre;
    private String rut;
    private String email;
    private String rol;
    private String telefono;
    private String comuna;
    private Long empresaId;
    private String area;
    private String cargo;
    private boolean activo;
    private String fechaIncorporacion;
    private Integer avisosDias;
    private boolean avisosActivos;
    private String empresa;
    private Long agenteAsignadoId;
    private String fotoUri;
    private String preguntaSeguridad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getFechaIncorporacion() {
        return fechaIncorporacion;
    }

    public void setFechaIncorporacion(String fechaIncorporacion) {
        this.fechaIncorporacion = fechaIncorporacion;
    }

    public Integer getAvisosDias() {
        return avisosDias;
    }

    public void setAvisosDias(Integer avisosDias) {
        this.avisosDias = avisosDias;
    }

    public boolean isAvisosActivos() {
        return avisosActivos;
    }

    public void setAvisosActivos(boolean avisosActivos) {
        this.avisosActivos = avisosActivos;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Long getAgenteAsignadoId() {
        return agenteAsignadoId;
    }

    public void setAgenteAsignadoId(Long agenteAsignadoId) {
        this.agenteAsignadoId = agenteAsignadoId;
    }

    public String getFotoUri() {
        return fotoUri;
    }

    public void setFotoUri(String fotoUri) {
        this.fotoUri = fotoUri;
    }

    public String getPreguntaSeguridad() {
        return preguntaSeguridad;
    }

    public void setPreguntaSeguridad(String preguntaSeguridad) {
        this.preguntaSeguridad = preguntaSeguridad;
    }
}
