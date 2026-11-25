package com.pagoflex.pagos.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Espejo JPA de {@code PagoEntity} de la app Android (Room, tabla "pagos").
 * Se agregan los identificadores de usuario, compromiso y empresa para que el
 * microservicio pueda resolver las consultas del panel ejecutivo.
 */
@Entity
@Table(
        name = "pagos",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_pago_folio_unico", columnNames = "folio_unico"),
                @UniqueConstraint(name = "uk_pago_numero_comprobante", columnNames = "numero_comprobante")
        },
        indexes = {
                @Index(name = "idx_pagos_usuario", columnList = "id_usuario"),
                @Index(name = "idx_pagos_compromiso", columnList = "id_compromiso"),
                @Index(name = "idx_pagos_empresa", columnList = "id_empresa"),
                @Index(name = "idx_pagos_fecha_pago", columnList = "fecha_pago")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "folio_unico", nullable = false, length = 40, unique = true)
    private String folioUnico;

    @Column(name = "numero_comprobante", nullable = false, length = 30, unique = true)
    private String numeroComprobante;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_compromiso", nullable = false)
    private Long idCompromiso;

    @Column(name = "id_empresa", nullable = false)
    private Long idEmpresa;

    @Column(name = "monto_pagado", nullable = false)
    private Integer montoPagado;

    @Column(name = "cuota_pagada")
    private Integer cuotaPagada;

    @Column(name = "metodo_pago", nullable = false, length = 50)
    private String metodoPago;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "fecha_vencimiento_cuota")
    private LocalDate fechaVencimientoCuota;

    @Column(name = "canal", nullable = false, length = 30)
    private String canal = "App";

    @Column(name = "foto_uri", length = 500)
    private String fotoUri;

    @OneToOne(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Comprobante comprobante;
}
