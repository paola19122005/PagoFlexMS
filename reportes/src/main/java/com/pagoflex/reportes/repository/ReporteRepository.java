package com.pagoflex.reportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pagoflex.reportes.entity.Reporte;

public interface ReporteRepository extends JpaRepository<Reporte, Long>{

    List<Reporte> findAllByOrderByIdDesc();

    List<Reporte> findByIdUsuarioOrderByIdDesc(Long idUsuario);
}
