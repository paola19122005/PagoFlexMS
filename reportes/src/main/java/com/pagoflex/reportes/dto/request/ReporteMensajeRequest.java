package com.pagoflex.reportes.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReporteMensajeRequest {

    @NotNull
    private Long idRemitente;

    @NotBlank
    @Size(max = 20)
    private String rolRemitente;

    @NotBlank
    @Size(max = 255)
    private String contenido;

    @NotBlank
    @Size(max = 30)
    private String fechaHora;

    public ReporteMensajeRequest() {
    }

    public ReporteMensajeRequest(Long idRemitente, String rolRemitente, String contenido, String fechaHora) {
        this.idRemitente = idRemitente;
        this.rolRemitente = rolRemitente;
        this.contenido = contenido;
        this.fechaHora = fechaHora;
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
