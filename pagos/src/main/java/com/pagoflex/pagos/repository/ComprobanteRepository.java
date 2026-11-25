package com.pagoflex.pagos.repository;

import com.pagoflex.pagos.entity.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {

    Optional<Comprobante> findByPagoId(Long idPago);

    Optional<Comprobante> findByPagoFolioUnico(String folioUnico);
}
