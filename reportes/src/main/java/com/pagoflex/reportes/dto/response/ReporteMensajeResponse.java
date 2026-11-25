package com.pagoflex.reportes.dto.response;

public class ReporteMensajeResponse {

    private Long id;
    private Long idReporte;
    private Long idRemitente;
    private String rolRemitente;
    private String contenido;
    private String fechaHora;

    public ReporteMensajeResponse() {
    }

    public ReporteMensajeResponse(Long id, Long idReporte, Long idRemitente, String rolRemitente, String contenido, String fechaHora) {
        this.id = id;
        this.idReporte = idReporte;
        this.idRemitente = idRemitente;
        this.rolRemitente = rolRemitente;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
    }

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

    @Override
    public String toString() {
        return "ReporteMensajeResponse{" +
                "id=" + id +
                ", idReporte=" + idReporte +
                ", idRemitente=" + idRemitente +
                ", rolRemitente='" + rolRemitente + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                '}';
    }
}
