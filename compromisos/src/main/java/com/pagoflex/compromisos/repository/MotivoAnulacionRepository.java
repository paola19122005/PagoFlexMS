package com.pagoflex.compromisos.repository;

import com.pagoflex.compromisos.entity.MotivoAnulacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MotivoAnulacionRepository extends JpaRepository<MotivoAnulacion, Integer> {

    Optional<MotivoAnulacion> findByNombre(String nombre);
}
