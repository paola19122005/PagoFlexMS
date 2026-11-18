package com.pagoflex.compromisos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "motivo_anulacion")
@Getter
@Setter
@NoArgsConstructor
public class MotivoAnulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_motivo_anulacion", nullable = false)
    private Integer idMotivoAnulacion;

    @Column(name = "nombre", nullable = false, length = 60, unique = true)
    private String nombre;
}
