package com.pagoflex.agente.repository;

import com.pagoflex.agente.entity.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {
    List<Mensaje> findByIdConversacionOrderByIdAsc(Long idConversacion);
}
