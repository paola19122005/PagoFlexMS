package com.pagoflex.reportes.dto.response;

public class ReporteMensajeResponse {

    private Long id;
    private Long idReporte;
    private Long idRemitente;
    private String rolRemitente;
    private String contenido;
    private String fechaHora;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

     public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public Long getIdRemitente() {
        return idRemitente;
    }

    public void setIdRemitente(Long idRemitente) {
        this.idRemitente = idRemitente;
    }

    public String getRolRemitente() {
        return rolRemitente;
    }

    public void setRolRemitente(String rolRemitente) {
        this.rolRemitente = rolRemitente;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
}
