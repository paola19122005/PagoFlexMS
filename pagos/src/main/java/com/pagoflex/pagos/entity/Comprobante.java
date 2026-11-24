package com.pagoflex.pagos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Espejo JPA de {@code ComprobanteEntity} de la app Android (Room, tabla
 * "comprobantes"). Cada pago tiene a lo mas un comprobante asociado.
 */
@Entity
@Table(
        name = "comprobantes",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_comprobante_pago", columnNames = "id_pago")
        },
        indexes = {
                @Index(name = "idx_comprobantes_pago", columnList = "id_pago")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pago", nullable = false, unique = true)
    private Pago pago;

    @Column(name = "uri", nullable = false, length = 500)
    private String uri;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;
}
