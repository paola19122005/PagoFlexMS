package com.pagoflex.agente.repository;

import com.pagoflex.agente.entity.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {
    List<Conversacion> findByIdUsuario(Long idUsuario);
}
