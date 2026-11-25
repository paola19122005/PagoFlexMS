package com.pagoflex.empresas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
public class Empresas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    private String codigo;

    @Column(name = "razon_social", nullable = false, length = 100)
    private String razonSocial;

    @Column(name = "nombre_fantasia", nullable = false, length = 100)
    private String nombreFantasia;

    @Column(name = "rut", nullable = false, unique = true, length = 20)
    private String rut;

    @Column(name = "rubro", nullable = false, length = 100)
    private String rubro;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "telefono", nullable = false, length = 20)
    private String telefono;

    @Column(name = "recargo")
    private Integer recargo;
}
