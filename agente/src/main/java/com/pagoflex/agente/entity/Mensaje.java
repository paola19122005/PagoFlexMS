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
        name = "mensajes",
        indexes = {
                @Index(name = "idx_mensajes_conversacion", columnList = "id_conversacion")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "id_conversacion", nullable = false)
    private Long idConversacion;

    @Column(name = "id_remitente", nullable = false)
    private Long idRemitente;

    @Column(name = "rol_remitente", nullable = false, length = 20)
    private String rolRemitente;

    @Column(name = "contenido", nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(name = "fecha_hora", nullable = false, length = 20)
    private String fechaHora;
}
