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
@Table(name = "estado_compromiso")
@Getter
@Setter
@NoArgsConstructor
public class EstadoCompromiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_compromiso", nullable = false)
    private Integer idEstadoCompromiso;

    @Column(name = "nombre", nullable = false, length = 30, unique = true)
    private String nombre;
}
