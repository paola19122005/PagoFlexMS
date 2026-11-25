package com.pagoflex.compromisos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "anulacion")
@Getter
@Setter
@NoArgsConstructor
public class Anulacion {

    @Id
    @Column(name = "id_compromiso", nullable = false)
    private Integer idCompromiso;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_compromiso")
    private Compromiso compromiso;

    @Column(name = "id_ejecutivo", nullable = false)
    private Integer idEjecutivo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_motivo_anulacion", nullable = false)
    private MotivoAnulacion motivo;

    @Column(name = "comentario", length = 255)
    private String comentario;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
}
