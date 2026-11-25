package com.pagoflex.pagos.dto.response;

import java.time.LocalDateTime;

/**
 * Resumen de recaudacion utilizado por el panel del ejecutivo.
 */
public class PagoResumenResponse {

    private Long empresaId;
    private Long cantidadPagos;
    private Long totalRecaudado;
    private LocalDateTime fechaGeneracion;

    public Long getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    public Long getCantidadPagos() {
        return cantidadPagos;
    }

    public void setCantidadPagos(Long cantidadPagos) {
        this.cantidadPagos = cantidadPagos;
    }

    public Long getTotalRecaudado() {
        return totalRecaudado;
    }

    public void setTotalRecaudado(Long totalRecaudado) {
        this.totalRecaudado = totalRecaudado;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}
