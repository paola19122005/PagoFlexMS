package com.pagoflex.usuarios.entity;

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

/**
 * Espejo JPA de {@code UserEntity} de la app Android (Room, tabla "usuarios").
 * Los campos, tipos y formatos se mantienen iguales para que la app pueda
 * consumir este microservicio sin transformaciones.
 */
@Entity
@Table(
        name = "usuarios",
        indexes = {
                @Index(name = "idx_usuarios_rol", columnList = "rol"),
                @Index(name = "idx_usuarios_empresa", columnList = "empresa_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "codigo", length = 20, unique = true)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "rut", length = 15)
    private String rut;

    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash = "";

    @Column(name = "salt", nullable = false, length = 64)
    private String salt = "";

    @Column(name = "rol", nullable = false, length = 20)
    private String rol;

    @Column(name = "telefono", length = 30)
    private String telefono;

    @Column(name = "comuna", length = 60)
    private String comuna;

    @Column(name = "empresa_id")
    private Long empresaId;

    @Column(name = "area", length = 60)
    private String area;

    @Column(name = "cargo", length = 80)
    private String cargo;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;

    /** Fecha en formato dd-MM-uuuu, igual que Room. */
    @Column(name = "fecha_incorporacion", length = 10)
    private String fechaIncorporacion;

    @Column(name = "avisos_dias")
    private Integer avisosDias = 3;

    @Column(name = "avisos_activos", nullable = false)
    private boolean avisosActivos = true;

    /** Nombre de fantasia de la empresa, desnormalizado igual que en Room. */
    @Column(name = "empresa", length = 120)
    private String empresa;

    @Column(name = "agente_asignado_id")
    private Long agenteAsignadoId;

    @Column(name = "foto_uri", length = 500)
    private String fotoUri;

    @Column(name = "pregunta_seguridad", nullable = false, length = 120)
    private String preguntaSeguridad;

    @Column(name = "respuesta_seguridad", nullable = false, length = 120)
    private String respuestaSeguridad;
}
