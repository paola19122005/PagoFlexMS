package com.pagoflex.compromisos.dto.response;

import java.time.LocalDateTime;

public class AnulacionResponse {

    private Integer idCompromiso;
    private Integer idEjecutivo;
    private String motivo;
    private String comentario;
    private LocalDateTime fecha;
    private String estadoCompromiso;

    public Integer getIdCompromiso() {
        return idCompromiso;
    }

    public void setIdCompromiso(Integer idCompromiso) {
        this.idCompromiso = idCompromiso;
    }

    public Integer getIdEjecutivo() {
        return idEjecutivo;
    }

    public void setIdEjecutivo(Integer idEjecutivo) {
        this.idEjecutivo = idEjecutivo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getEstadoCompromiso() {
        return estadoCompromiso;
    }

    public void setEstadoCompromiso(String estadoCompromiso) {
        this.estadoCompromiso = estadoCompromiso;
    }
}
