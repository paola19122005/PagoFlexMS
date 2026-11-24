package com.pagoflex.pagos.repository;

import com.pagoflex.pagos.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByFolioUnico(String folioUnico);

    List<Pago> findByIdUsuario(Long idUsuario);

    List<Pago> findByIdCompromiso(Long idCompromiso);

    List<Pago> findByIdEmpresa(Long idEmpresa);

    Optional<Pago> findTopByIdCompromisoOrderByFechaPagoDesc(Long idCompromiso);

    boolean existsByFolioUnico(String folioUnico);

    boolean existsByNumeroComprobante(String numeroComprobante);
}
