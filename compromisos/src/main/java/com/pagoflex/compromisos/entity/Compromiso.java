package com.pagoflex.compromisos.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "compromiso",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_compromiso_folio", columnNames = "folio"),
                @UniqueConstraint(name = "uk_compromiso_unicidad", columnNames = {
                        "id_usuario_final", "id_empresa", "concepto", "numero_cuota_normalizado"
                })
        },
        indexes = {
                @Index(name = "idx_compromiso_usuario", columnList = "id_usuario_final"),
                @Index(name = "idx_compromiso_empresa", columnList = "id_empresa"),
                @Index(name = "idx_compromiso_estado", columnList = "id_estado_compromiso"),
                @Index(name = "idx_compromiso_vencimiento", columnList = "fecha_vencimiento")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Compromiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compromiso", nullable = false)
    private Integer idCompromiso;

    @Column(name = "folio", nullable = false, length = 20, unique = true)
    private String folio;

    @Column(name = "id_usuario_final", nullable = false)
    private Integer idUsuarioFinal;

    @Column(name = "id_empresa", nullable = false)
    private Integer idEmpresa;

    @Column(name = "concepto", nullable = false, length = 120)
    private String concepto;

    @Column(name = "numero_cuota")
    private Short numeroCuota;

    @Column(name = "total_cuotas")
    private Short totalCuotas;

    @Column(name = "numero_cuota_normalizado", insertable = false, updatable = false)
    private Short numeroCuotaNormalizado;

    @Column(name = "monto", nullable = false)
    private Integer monto;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado_compromiso", nullable = false)
    private EstadoCompromiso estado;

    @Column(name = "recargo_aplicado", nullable = false)
    private Integer recargoAplicado = 0;

    @Column(name = "fecha_cambio_estado", nullable = false)
    private LocalDateTime fechaCambioEstado;

    @Column(name = "id_ejecutivo_registro")
    private Integer idEjecutivoRegistro;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "sistema_origen", length = 30)
    private String sistemaOrigen;

    @Column(name = "id_instancia_origen", length = 36)
    private String idInstanciaOrigen;

    @Column(name = "id_compromiso_origen")
    private Long idCompromisoOrigen;

    @OneToOne(mappedBy = "compromiso", fetch = FetchType.LAZY)
    private Anulacion anulacion;
}
