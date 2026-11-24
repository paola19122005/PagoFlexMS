package com.pagoflex.reportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pagoflex.reportes.entity.Reporte;
import com.pagoflex.reportes.entity.ReporteMensaje;

public interface ReporteRepository extends JpaRepository<Reporte, Long>{

    List<Reporte> findAllByOrderByIdDesc();

    List<Reporte> findByIdUsuarioOrderByIdDesc(Long idUsuario);

    @Query("SELECT m FROM ReporteMensaje m WHERE m.reporte.id = :idReporte ORDER BY m.id ASC")
    List<ReporteMensaje> findByIdReporteOrderByIdAsc(Long idReporte);
}
