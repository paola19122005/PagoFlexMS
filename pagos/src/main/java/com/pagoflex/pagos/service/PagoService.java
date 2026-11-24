package com.pagoflex.pagos.service;

import com.pagoflex.pagos.dto.request.PagoRegistroRequest;
import com.pagoflex.pagos.dto.response.ComprobanteResponse;
import com.pagoflex.pagos.dto.response.CompromisoRemotoResponse;
import com.pagoflex.pagos.dto.response.PagoResumenResponse;
import com.pagoflex.pagos.dto.response.PagoResponse;
import com.pagoflex.pagos.dto.response.UsuarioRemotoResponse;
import com.pagoflex.pagos.entity.Comprobante;
import com.pagoflex.pagos.entity.Pago;
import com.pagoflex.pagos.exception.ComprobanteNoEncontradoException;
import com.pagoflex.pagos.exception.EstadoCompromisoInvalidoException;
import com.pagoflex.pagos.exception.FolioDuplicadoException;
import com.pagoflex.pagos.exception.PagoDuplicadoException;
import com.pagoflex.pagos.exception.PagoNoEncontradoException;
import com.pagoflex.pagos.exception.UsuarioInactivoException;
import com.pagoflex.pagos.mapper.PagoMapper;
import com.pagoflex.pagos.repository.ComprobanteRepository;
import com.pagoflex.pagos.repository.PagoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class PagoService {

    private static final int MAX_INTENTOS_IDENTIFICADOR = 5;
    private static final String ESTADO_PAGADO = "PAGADO";
    private static final String ESTADO_ANULADO = "ANULADO";
    private static final String ESTADO_EN_REVISION = "EN_REVISION";

    private final PagoRepository pagoRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final PagoMapper pagoMapper;
    private final FolioGenerator folioGenerator;
    private final UsuarioIntegrationService usuarioIntegrationService;
    private final CompromisoIntegrationService compromisoIntegrationService;

    public PagoService(PagoRepository pagoRepository,
                       ComprobanteRepository comprobanteRepository,
                       PagoMapper pagoMapper,
                       FolioGenerator folioGenerator,
                       UsuarioIntegrationService usuarioIntegrationService,
                       CompromisoIntegrationService compromisoIntegrationService) {
        this.pagoRepository = pagoRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.pagoMapper = pagoMapper;
        this.folioGenerator = folioGenerator;
        this.usuarioIntegrationService = usuarioIntegrationService;
        this.compromisoIntegrationService = compromisoIntegrationService;
    }

    /**
     * Registra el pago de una cuota. Valida usuario y compromiso contra sus
     * microservicios, genera el folio unico cuando no viene informado y deja
     * la cuota del compromiso en estado PAGADO.
     */
    @Transactional
    public PagoResponse registrar(PagoRegistroRequest request) {
        validarSolicitud(request);

        UsuarioRemotoResponse usuario = usuarioIntegrationService.obtenerUsuario(request.getUsuarioId());
        if (Boolean.FALSE.equals(usuario.getActivo())) {
            throw new UsuarioInactivoException(
                    "El usuario " + request.getUsuarioId() + " se encuentra deshabilitado");
        }

        CompromisoRemotoResponse compromiso =
                compromisoIntegrationService.obtenerCompromiso(request.getCompromisoId());
        validarCompromiso(request, compromiso);

        String folioUnico = resolverFolioUnico(request.getFolioUnico());
        String numeroComprobante = resolverNumeroComprobante(request.getNumeroComprobante());

        LocalDateTime fechaPago = request.getFechaPago() != null
                ? request.getFechaPago()
                : LocalDateTime.now();
        Integer cuotaPagada = request.getCuotaPagada() != null
                ? request.getCuotaPagada()
                : (compromiso.getNumeroCuota() != null ? compromiso.getNumeroCuota().intValue() : null);
        LocalDate fechaVencimientoCuota = request.getFechaVencimientoCuota() != null
                ? request.getFechaVencimientoCuota()
                : compromiso.getFechaVencimiento();

        Pago pago = pagoMapper.toEntity(
                request,
                folioUnico,
                numeroComprobante,
                fechaPago,
                cuotaPagada,
                fechaVencimientoCuota
        );

        Pago guardado = pagoRepository.save(pago);
        compromisoIntegrationService.marcarComoPagado(compromiso);

        return pagoMapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public PagoResponse obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new PagoNoEncontradoException("Pago no encontrado: " + id));
        return pagoMapper.toResponse(pago);
    }

    @Transactional(readOnly = true)
    public PagoResponse obtenerPorFolioUnico(String folioUnico) {
        if (folioUnico == null || folioUnico.isBlank()) {
            throw new IllegalArgumentException("El folio unico es obligatorio");
        }
        Pago pago = pagoRepository.findByFolioUnico(folioUnico.trim())
                .orElseThrow(() -> new PagoNoEncontradoException(
                        "Pago no encontrado para el folio unico: " + folioUnico));
        return pagoMapper.toResponse(pago);
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorUsuario(Long usuarioId) {
        return pagoRepository.findByIdUsuario(usuarioId).stream()
                .map(pagoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorCompromiso(Long compromisoId) {
        return pagoRepository.findByIdCompromiso(compromisoId).stream()
                .map(pagoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorEmpresa(Long empresaId) {
        return pagoRepository.findByIdEmpresa(empresaId).stream()
                .map(pagoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PagoResponse> listarTodos() {
        return pagoRepository.findAll().stream()
                .map(pagoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagoResumenResponse resumenPorEmpresa(Long empresaId) {
        List<Pago> pagos = pagoRepository.findByIdEmpresa(empresaId);
        long totalRecaudado = pagos.stream()
                .mapToLong(pago -> pago.getMontoPagado() != null ? pago.getMontoPagado() : 0)
                .sum();

        PagoResumenResponse resumen = new PagoResumenResponse();
        resumen.setEmpresaId(empresaId);
        resumen.setCantidadPagos((long) pagos.size());
        resumen.setTotalRecaudado(totalRecaudado);
        resumen.setFechaGeneracion(LocalDateTime.now());
        return resumen;
    }

    @Transactional(readOnly = true)
    public ComprobanteResponse obtenerComprobantePorPago(Long pagoId) {
        Comprobante comprobante = comprobanteRepository.findByPagoId(pagoId)
                .orElseThrow(() -> new ComprobanteNoEncontradoException(
                        "Comprobante no encontrado para el pago: " + pagoId));
        return pagoMapper.toResponse(comprobante);
    }

    @Transactional(readOnly = true)
    public ComprobanteResponse obtenerComprobantePorFolio(String folioUnico) {
        if (folioUnico == null || folioUnico.isBlank()) {
            throw new IllegalArgumentException("El folio unico es obligatorio");
        }
        Comprobante comprobante = comprobanteRepository.findByPagoFolioUnico(folioUnico.trim())
                .orElseThrow(() -> new ComprobanteNoEncontradoException(
                        "Comprobante no encontrado para el folio unico: " + folioUnico));
        return pagoMapper.toResponse(comprobante);
    }

    private void validarSolicitud(PagoRegistroRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud de registro de pago no puede ser nula");
        }
        if (request.getUsuarioId() == null || request.getUsuarioId() <= 0) {
            throw new IllegalArgumentException("El usuarioId es obligatorio");
        }
        if (request.getCompromisoId() == null || request.getCompromisoId() <= 0) {
            throw new IllegalArgumentException("El compromisoId es obligatorio");
        }
        if (request.getEmpresaId() == null || request.getEmpresaId() <= 0) {
            throw new IllegalArgumentException("El empresaId es obligatorio");
        }
        if (request.getMontoPagado() == null || request.getMontoPagado() <= 0) {
            throw new IllegalArgumentException("El montoPagado debe ser mayor que cero");
        }
        if (request.getMetodoPago() == null || request.getMetodoPago().isBlank()) {
            throw new IllegalArgumentException("El metodoPago es obligatorio");
        }
        if (request.getCuotaPagada() != null && request.getCuotaPagada() <= 0) {
            throw new IllegalArgumentException("La cuotaPagada debe ser mayor que cero");
        }
    }

    private void validarCompromiso(PagoRegistroRequest request, CompromisoRemotoResponse compromiso) {
        if (compromiso.getIdUsuarioFinal() != null
                && !compromiso.getIdUsuarioFinal().equals(request.getUsuarioId())) {
            throw new IllegalArgumentException(
                    "El compromiso " + request.getCompromisoId() + " no pertenece al usuario indicado");
        }
        if (compromiso.getIdEmpresa() != null
                && !compromiso.getIdEmpresa().equals(request.getEmpresaId())) {
            throw new IllegalArgumentException(
                    "El compromiso " + request.getCompromisoId() + " no pertenece a la empresa indicada");
        }

        String estado = compromiso.getEstado() == null
                ? ""
                : compromiso.getEstado().trim().toUpperCase(Locale.ROOT);

        if (ESTADO_PAGADO.equals(estado)) {
            throw new PagoDuplicadoException(
                    "El compromiso " + request.getCompromisoId() + " ya se encuentra pagado");
        }
        if (ESTADO_ANULADO.equals(estado) || ESTADO_EN_REVISION.equals(estado)) {
            throw new EstadoCompromisoInvalidoException(
                    "El compromiso " + request.getCompromisoId()
                            + " no puede pagarse porque su estado es " + estado);
        }
    }

    private String resolverFolioUnico(String folioSolicitado) {
        if (folioSolicitado != null && !folioSolicitado.isBlank()) {
            String folio = folioSolicitado.trim().toUpperCase(Locale.ROOT);
            if (pagoRepository.existsByFolioUnico(folio)) {
                throw new FolioDuplicadoException("Ya existe un pago con el folio unico: " + folio);
            }
            return folio;
        }

        for (int intento = 0; intento < MAX_INTENTOS_IDENTIFICADOR; intento++) {
            String folio = folioGenerator.generarFolioUnico();
            if (!pagoRepository.existsByFolioUnico(folio)) {
                return folio;
            }
        }
        throw new FolioDuplicadoException("No fue posible generar un folio unico para el pago");
    }

    private String resolverNumeroComprobante(String numeroSolicitado) {
        if (numeroSolicitado != null && !numeroSolicitado.isBlank()) {
            String numero = numeroSolicitado.trim().toUpperCase(Locale.ROOT);
            if (pagoRepository.existsByNumeroComprobante(numero)) {
                throw new FolioDuplicadoException(
                        "Ya existe un pago con el numero de comprobante: " + numero);
            }
            return numero;
        }

        for (int intento = 0; intento < MAX_INTENTOS_IDENTIFICADOR; intento++) {
            String numero = folioGenerator.generarNumeroComprobante();
            if (!pagoRepository.existsByNumeroComprobante(numero)) {
                return numero;
            }
        }
        throw new FolioDuplicadoException("No fue posible generar un numero de comprobante para el pago");
    }
}
