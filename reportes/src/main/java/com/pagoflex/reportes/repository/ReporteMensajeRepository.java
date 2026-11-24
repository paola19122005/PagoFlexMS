package com.pagoflex.reportes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pagoflex.reportes.entity.ReporteMensaje;

public interface ReporteMensajeRepository 
        extends JpaRepository<ReporteMensaje, Long>{

    List<ReporteMensaje> findByIdReporteOrderByIdAsc(Long idReporte);
}
