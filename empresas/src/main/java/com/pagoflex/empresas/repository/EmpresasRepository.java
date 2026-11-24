package com.pagoflex.empresas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pagoflex.empresas.entity.Empresas;

public interface EmpresasRepository extends JpaRepository<Empresas, Long> {

    Optional<Empresas> findByCodigo(String codigo);

    Optional<Empresas> findByRut(String rut);

    boolean existsByCodigo(String codigo);

    boolean existsByRut(String rut);
}
