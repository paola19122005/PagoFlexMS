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
import com.pagoflex.pagos.exception.ComunicacionServicioException;
import com.pagoflex.pagos.exception.EstadoCompromisoInvalidoException;
import com.pagoflex.pagos.exception.FolioDuplicadoException;
import com.pagoflex.pagos.exception.PagoDuplicadoException;
import com.pagoflex.pagos.exception.PagoNoEncontradoException;
import com.pagoflex.pagos.exception.UsuarioInactivoException;
import com.pagoflex.pagos.mapper.PagoMapper;
import com.pagoflex.pagos.repository.ComprobanteRepository;
import com.pagoflex.pagos.repository.PagoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private ComprobanteRepository comprobanteRepository;
    @Spy
    private PagoMapper pagoMapper = new PagoMapper();
    @Mock
    private FolioGenerator folioGenerator;
    @Mock
    private UsuarioIntegrationService usuarioIntegrationService;
    @Mock
    private CompromisoIntegrationService compromisoIntegrationService;

    @InjectMocks
    private PagoService service;

    private PagoRegistroRequest requestValido() {
        PagoRegistroRequest request = new PagoRegistroRequest();
        request.setUsuarioId(7L);
        request.setCompromisoId(21L);
        request.setEmpresaId(1009L);
        request.setMontoPagado(15000);
        request.setMetodoPago("Pago simulado");
        request.setFechaPago(LocalDateTime.of(2026, 3, 5, 10, 30));
        return request;
    }

    private UsuarioRemotoResponse usuarioActivo() {
        UsuarioRemotoResponse usuario = new UsuarioRemotoResponse();
        usuario.setId(7L);
        usuario.setNombre("Camila Rojas");
        usuario.setEmail("camila@correo.cl");
        usuario.setRol("USUARIO");
        usuario.setActivo(true);
        return usuario;
    }

    private CompromisoRemotoResponse compromisoPendiente() {
        CompromisoRemotoResponse compromiso = new CompromisoRemotoResponse();
        compromiso.setIdCompromiso(21L);
        compromiso.setFolio("CMP-00021");
        compromiso.setIdUsuarioFinal(7L);
        compromiso.setIdEmpresa(1009L);
        compromiso.setNumeroCuota((short) 2);
        compromiso.setTotalCuotas((short) 4);
        compromiso.setMonto(15000);
        compromiso.setFechaVencimiento(LocalDate.of(2026, 3, 10));
        compromiso.setEstado("PENDIENTE");
        compromiso.setRecargoAplicado(0);
        return compromiso;
    }

    private CompromisoRemotoResponse prepararRemotos() {
        CompromisoRemotoResponse compromiso = compromisoPendiente();
        when(usuarioIntegrationService.obtenerUsuario(7L)).thenReturn(usuarioActivo());
        when(compromisoIntegrationService.obtenerCompromiso(21L)).thenReturn(compromiso);
        return compromiso;
    }

    private void prepararGuardado() {
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> {
            Pago guardado = invocation.getArgument(0);
            if (guardado.getId() == null) {
                guardado.setId(50L);
            }
            if (guardado.getComprobante() != null && guardado.getComprobante().getId() == null) {
                guardado.getComprobante().setId(70L);
            }
            return guardado;
        });
    }

    private Pago pago(Long id, Long usuarioId, Long empresaId, Integer monto) {
        Pago pago = new Pago();
        pago.setId(id);
        pago.setFolioUnico("PF-" + id);
        pago.setNumeroComprobante("CPR-" + id);
        pago.setIdUsuario(usuarioId);
        pago.setIdCompromiso(21L);
        pago.setIdEmpresa(empresaId);
        pago.setMontoPagado(monto);
        pago.setMetodoPago("Pago simulado");
        pago.setFechaPago(LocalDateTime.of(2026, 3, 5, 10, 30));
        return pago;
    }

    @Test
    void registrarGeneraFolioYAsociaComprobante() {
        PagoRegistroRequest request = requestValido();
        CompromisoRemotoResponse compromiso = prepararRemotos();
        when(folioGenerator.generarFolioUnico()).thenReturn("PF-TEST-0001");
        when(folioGenerator.generarNumeroComprobante()).thenReturn("CPR-TEST-0001");
        prepararGuardado();

        PagoResponse response = service.registrar(request);

        assertEquals(50L, response.getId());
        assertEquals("PF-TEST-0001", response.getFolioUnico());
        assertEquals("CPR-TEST-0001", response.getNumeroComprobante());
        assertEquals(7L, response.getUsuarioId());
        assertEquals(21L, response.getCompromisoId());
        assertEquals(1009L, response.getEmpresaId());
        assertEquals(2, response.getCuotaPagada());
        assertEquals(LocalDate.of(2026, 3, 10), response.getFechaVencimientoCuota());
        assertNotNull(response.getComprobante());
        assertEquals(70L, response.getComprobante().getId());
        assertEquals("pagoflex://comprobantes/PF-TEST-0001", response.getComprobante().getUri());

        ArgumentCaptor<Pago> captor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoRepository).save(captor.capture());
        Pago guardado = captor.getValue();
        assertEquals("PF-TEST-0001", guardado.getFolioUnico());
        assertEquals("CPR-TEST-0001", guardado.getNumeroComprobante());
        assertEquals(15000, guardado.getMontoPagado());
        assertEquals("Pago simulado", guardado.getMetodoPago());
        assertEquals("App", guardado.getCanal());
        assertSame(guardado, guardado.getComprobante().getPago());

        verify(compromisoIntegrationService).marcarComoPagado(compromiso);
    }

    @Test
    void registrarRespetaFolioInformadoPorElCliente() {
        PagoRegistroRequest request = requestValido();
        request.setFolioUnico("pf-cliente-01");
        request.setNumeroComprobante("cpr-cliente-01");
        prepararRemotos();
        prepararGuardado();

        PagoResponse response = service.registrar(request);

        assertEquals("PF-CLIENTE-01", response.getFolioUnico());
        assertEquals("CPR-CLIENTE-01", response.getNumeroComprobante());
        verifyNoInteractions(folioGenerator);
    }

    @Test
    void registrarRechazaCompromisoYaPagado() {
        PagoRegistroRequest request = requestValido();
        when(usuarioIntegrationService.obtenerUsuario(7L)).thenReturn(usuarioActivo());
        CompromisoRemotoResponse compromiso = compromisoPendiente();
        compromiso.setEstado("PAGADO");
        when(compromisoIntegrationService.obtenerCompromiso(21L)).thenReturn(compromiso);

        assertThrows(PagoDuplicadoException.class, () -> service.registrar(request));
        verify(pagoRepository, never()).save(any());
        verifyNoInteractions(folioGenerator);
    }

    @Test
    void registrarRechazaUsuarioInactivo() {
        PagoRegistroRequest request = requestValido();
        UsuarioRemotoResponse usuario = usuarioActivo();
        usuario.setActivo(false);
        when(usuarioIntegrationService.obtenerUsuario(7L)).thenReturn(usuario);

        assertThrows(UsuarioInactivoException.class, () -> service.registrar(request));
        verifyNoInteractions(compromisoIntegrationService);
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void registrarRechazaCompromisoDeOtroUsuario() {
        PagoRegistroRequest request = requestValido();
        when(usuarioIntegrationService.obtenerUsuario(7L)).thenReturn(usuarioActivo());
        CompromisoRemotoResponse compromiso = compromisoPendiente();
        compromiso.setIdUsuarioFinal(99L);
        when(compromisoIntegrationService.obtenerCompromiso(21L)).thenReturn(compromiso);

        assertThrows(IllegalArgumentException.class, () -> service.registrar(request));
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void registrarRechazaCompromisoAnulado() {
        PagoRegistroRequest request = requestValido();
        when(usuarioIntegrationService.obtenerUsuario(7L)).thenReturn(usuarioActivo());
        CompromisoRemotoResponse compromiso = compromisoPendiente();
        compromiso.setEstado("ANULADO");
        when(compromisoIntegrationService.obtenerCompromiso(21L)).thenReturn(compromiso);

        assertThrows(EstadoCompromisoInvalidoException.class, () -> service.registrar(request));
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void registrarRechazaFolioDuplicado() {
        PagoRegistroRequest request = requestValido();
        request.setFolioUnico("PF-DUPLICADO");
        prepararRemotos();
        when(pagoRepository.existsByFolioUnico("PF-DUPLICADO")).thenReturn(true);

        assertThrows(FolioDuplicadoException.class, () -> service.registrar(request));
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void registrarPropagaFalloDeComunicacion() {
        PagoRegistroRequest request = requestValido();
        when(usuarioIntegrationService.obtenerUsuario(7L))
                .thenThrow(new ComunicacionServicioException("servicio caido"));

        assertThrows(ComunicacionServicioException.class, () -> service.registrar(request));
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void listarPorUsuarioMapeaPagos() {
        when(pagoRepository.findByIdUsuario(7L))
                .thenReturn(List.of(pago(1L, 7L, 1009L, 15000), pago(2L, 7L, 1009L, 8000)));

        List<PagoResponse> pagos = service.listarPorUsuario(7L);

        assertEquals(2, pagos.size());
        assertEquals("PF-1", pagos.getFirst().getFolioUnico());
    }

    @Test
    void listarPorCompromisoMapeaPagos() {
        when(pagoRepository.findByIdCompromiso(21L)).thenReturn(List.of(pago(1L, 7L, 1009L, 15000)));

        assertEquals(1, service.listarPorCompromiso(21L).size());
    }

    @Test
    void listarPorEmpresaYResumenRecaudado() {
        when(pagoRepository.findByIdEmpresa(1009L))
                .thenReturn(List.of(pago(1L, 7L, 1009L, 15000), pago(2L, 8L, 1009L, 8000)));

        assertEquals(2, service.listarPorEmpresa(1009L).size());

        PagoResumenResponse resumen = service.resumenPorEmpresa(1009L);

        assertEquals(1009L, resumen.getEmpresaId());
        assertEquals(2L, resumen.getCantidadPagos());
        assertEquals(23000L, resumen.getTotalRecaudado());
        assertNotNull(resumen.getFechaGeneracion());
    }

    @Test
    void listarTodosMapeaPagos() {
        when(pagoRepository.findAll()).thenReturn(List.of(pago(1L, 7L, 1009L, 15000)));

        assertEquals(1, service.listarTodos().size());
    }

    @Test
    void obtenerPorIdEncontrado() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(pago(1L, 7L, 1009L, 15000)));

        assertEquals(1L, service.obtenerPorId(1L).getId());
    }

    @Test
    void obtenerPorIdNoEncontrado() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PagoNoEncontradoException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void obtenerPorFolioUnicoEncontrado() {
        Pago pago = pago(1L, 7L, 1009L, 15000);
        pago.setFolioUnico("PF-FOLIO-01");
        when(pagoRepository.findByFolioUnico("PF-FOLIO-01")).thenReturn(Optional.of(pago));

        assertEquals("PF-FOLIO-01", service.obtenerPorFolioUnico("PF-FOLIO-01").getFolioUnico());
    }

    @Test
    void obtenerPorFolioUnicoNoEncontrado() {
        when(pagoRepository.findByFolioUnico("PF-NO-EXISTE")).thenReturn(Optional.empty());

        assertThrows(PagoNoEncontradoException.class,
                () -> service.obtenerPorFolioUnico("PF-NO-EXISTE"));
    }

    @Test
    void obtenerComprobantePorPagoEncontrado() {
        Pago pago = pago(1L, 7L, 1009L, 15000);
        Comprobante comprobante = new Comprobante();
        comprobante.setId(70L);
        comprobante.setPago(pago);
        comprobante.setUri("pagoflex://comprobantes/PF-1");
        comprobante.setFecha(LocalDateTime.of(2026, 3, 5, 10, 30));
        when(comprobanteRepository.findByPagoId(1L)).thenReturn(Optional.of(comprobante));

        ComprobanteResponse response = service.obtenerComprobantePorPago(1L);

        assertEquals(70L, response.getId());
        assertEquals(1L, response.getIdPago());
    }

    @Test
    void obtenerComprobantePorPagoNoEncontrado() {
        when(comprobanteRepository.findByPagoId(99L)).thenReturn(Optional.empty());

        assertThrows(ComprobanteNoEncontradoException.class,
                () -> service.obtenerComprobantePorPago(99L));
    }

    @Test
    void obtenerComprobantePorFolioEncontrado() {
        Pago pago = pago(1L, 7L, 1009L, 15000);
        pago.setFolioUnico("PF-FOLIO-01");
        Comprobante comprobante = new Comprobante();
        comprobante.setId(70L);
        comprobante.setPago(pago);
        comprobante.setUri("pagoflex://comprobantes/PF-FOLIO-01");
        comprobante.setFecha(LocalDateTime.of(2026, 3, 5, 10, 30));
        when(comprobanteRepository.findByPagoFolioUnico("PF-FOLIO-01"))
                .thenReturn(Optional.of(comprobante));

        assertEquals(70L, service.obtenerComprobantePorFolio("PF-FOLIO-01").getId());
    }
}
