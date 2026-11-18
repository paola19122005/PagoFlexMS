package com.pagoflex.compromisos.repository;

import com.pagoflex.compromisos.entity.EstadoCompromiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoCompromisoRepository extends JpaRepository<EstadoCompromiso, Integer> {

    Optional<EstadoCompromiso> findByNombre(String nombre);
}
