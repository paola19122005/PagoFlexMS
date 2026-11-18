package com.pagoflex.compromisos.repository;

import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CompromisoRepository extends JpaRepository<Compromiso, Integer> {

    Optional<Compromiso> findByFolio(String folio);

    List<Compromiso> findByIdUsuarioFinal(Integer idUsuarioFinal);

    List<Compromiso> findByIdEmpresa(Integer idEmpresa);

    List<Compromiso> findByIdUsuarioFinalAndIdEmpresa(Integer idUsuarioFinal, Integer idEmpresa);

    List<Compromiso> findByEstado(EstadoCompromiso estado);

    List<Compromiso> findByEstadoAndFechaVencimientoBefore(EstadoCompromiso estado, LocalDate fecha);

    boolean existsByFolio(String folio);

    boolean existsByIdUsuarioFinalAndIdEmpresaAndConceptoAndNumeroCuota(
            Integer idUsuarioFinal,
            Integer idEmpresa,
            String concepto,
            Short numeroCuota
    );
}
