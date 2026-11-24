package com.pagoflex.reportes.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reporte_mensajes")
@Getter
@Setter
@NoArgsConstructor
public class ReporteMensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_reporte", nullable = false)
    private Long idReporte;

    @Column(name = "id_remitente", nullable = false)
    private Long idRemitente;

    @Column(name = "rol_remitente", nullable = false, length = 20)
    private String rolRemitente;

    @Column(name = "contenido", nullable = false, length = 255)
    private String contenido;

    @Column(name = "fecha_hora", nullable = false, length = 30)
    private String fechaHora;
}
