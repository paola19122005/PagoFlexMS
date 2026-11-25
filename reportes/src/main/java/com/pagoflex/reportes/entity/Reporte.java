package com.pagoflex.reportes.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reportes")
@Getter
@Setter
@NoArgsConstructor
public class Reporte {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "tipo", nullable = false, length = 100)
    private String tipo;

    @Column(name = "compromiso", nullable = false, length = 100)
    private String compromiso;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "fecha", nullable = false, length = 20)
    private String fecha;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "PENDIENTE";

    @OneToMany(mappedBy = "reporte", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReporteMensaje> mensajes = new ArrayList<>();
}
