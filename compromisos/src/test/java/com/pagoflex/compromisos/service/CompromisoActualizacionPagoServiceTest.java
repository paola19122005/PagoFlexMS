package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoActualizacionPagoRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.EstadoNoEncontradoException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompromisoActualizacionPagoServiceTest {

    @Mock
    private CompromisoRepository compromisoRepository;
    @Mock
    private EstadoCompromisoRepository estadoCompromisoRepository;
    @Mock
    private CompromisoMapper compromisoMapper;

    @InjectMocks
    private CompromisoActualizacionPagoService service;

    @Test
    void actualizaPorPago() {
        Compromiso compromiso = new Compromiso();
        compromiso.setEstado(new EstadoCompromiso());
        compromiso.getEstado().setNombre("Pendiente");

        EstadoCompromiso estadoNuevo = new EstadoCompromiso();
        estadoNuevo.setNombre("Pagado");

        CompromisoActualizacionPagoRequest request = new CompromisoActualizacionPagoRequest();
        request.setEstado("Pagado");
        request.setNumeroCuota((short) 2);
        request.setFechaVencimiento(LocalDate.of(2026, 1, 1));
        request.setRecargoAplicado(5);

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(estadoCompromisoRepository.findByNombre("Pagado")).thenReturn(Optional.of(estadoNuevo));
        when(compromisoRepository.save(compromiso)).thenReturn(compromiso);
        when(compromisoMapper.toResponse(compromiso)).thenReturn(new CompromisoResponse());

        CompromisoResponse response = service.actualizarPorPago(1, request);

        assertNotNull(response);
        assertEquals("Pagado", compromiso.getEstado().getNombre());
        assertEquals((short) 2, compromiso.getNumeroCuota());
    }

    @Test
    void lanzaNotFound() {
        CompromisoActualizacionPagoRequest request = new CompromisoActualizacionPagoRequest();
        request.setEstado("Pagado");
        request.setFechaVencimiento(LocalDate.of(2026, 1, 1));
        request.setRecargoAplicado(0);
        when(compromisoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(CompromisoNoEncontradoException.class, () -> service.actualizarPorPago(1, request));
    }

    @Test
    void lanzaEstadoNoEncontrado() {
        Compromiso compromiso = new Compromiso();
        compromiso.setEstado(new EstadoCompromiso());
        CompromisoActualizacionPagoRequest request = new CompromisoActualizacionPagoRequest();
        request.setEstado("X");
        request.setFechaVencimiento(LocalDate.of(2026, 1, 1));
        request.setRecargoAplicado(0);

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(estadoCompromisoRepository.findByNombre("X")).thenReturn(Optional.empty());

        assertThrows(EstadoNoEncontradoException.class, () -> service.actualizarPorPago(1, request));
    }
}
