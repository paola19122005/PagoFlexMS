package com.pagoflex.agente.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "conversaciones",
        indexes = {
                @Index(name = "idx_conversaciones_usuario", columnList = "id_usuario"),
                @Index(name = "idx_conversaciones_estado", columnList = "estado")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Conversacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "fecha_creacion", nullable = false, length = 20)
    private String fechaCreacion;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "PENDIENTE";
}
