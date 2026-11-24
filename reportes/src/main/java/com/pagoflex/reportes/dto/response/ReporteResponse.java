package com.pagoflex.reportes.dto.response;

public class ReporteResponse {

    private Long id;
    private String tipo;
    private String compromiso;
    private String descripcion;
    private String fecha;
    private Long idUsuario;
    private String estado;

    public ReporteResponse() {
    }

    public ReporteResponse(Long id, String tipo, String compromiso, String descripcion, String fecha, Long idUsuario, String estado) {
        this.id = id;
        this.tipo = tipo;
        this.compromiso = compromiso;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.idUsuario = idUsuario;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCompromiso() {
        return compromiso;
    }

    public void setCompromiso(String compromiso) {
        this.compromiso = compromiso;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "ReporteResponse{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", compromiso='" + compromiso + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", idUsuario=" + idUsuario +
                ", estado='" + estado + '\'' +
                '}';
    }
}
