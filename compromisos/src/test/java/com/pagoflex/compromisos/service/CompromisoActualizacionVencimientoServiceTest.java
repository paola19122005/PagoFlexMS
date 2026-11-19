package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoActualizacionVencimientoRequest;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompromisoActualizacionVencimientoServiceTest {

    @Mock
    private CompromisoRepository compromisoRepository;
    @Mock
    private EstadoCompromisoRepository estadoCompromisoRepository;
    @Mock
    private CompromisoMapper compromisoMapper;

    @InjectMocks
    private CompromisoActualizacionVencimientoService service;

    @Test
    void actualizaPorVencimiento() {
        Compromiso compromiso = new Compromiso();
        compromiso.setEstado(new EstadoCompromiso());
        compromiso.getEstado().setNombre("Pendiente");

        EstadoCompromiso estadoNuevo = new EstadoCompromiso();
        estadoNuevo.setNombre("Vencido");

        CompromisoActualizacionVencimientoRequest request = new CompromisoActualizacionVencimientoRequest();
        request.setEstado("Vencido");
        request.setRecargoAplicado(10);

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(estadoCompromisoRepository.findByNombre("Vencido")).thenReturn(Optional.of(estadoNuevo));
        when(compromisoRepository.save(compromiso)).thenReturn(compromiso);
        when(compromisoMapper.toResponse(compromiso)).thenReturn(new CompromisoResponse());

        service.actualizarPorVencimiento(1, request);

        assertEquals("Vencido", compromiso.getEstado().getNombre());
        assertEquals(10, compromiso.getRecargoAplicado());
    }

    @Test
    void lanzaNotFound() {
        CompromisoActualizacionVencimientoRequest request = new CompromisoActualizacionVencimientoRequest();
        request.setEstado("Vencido");
        request.setRecargoAplicado(0);
        when(compromisoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(CompromisoNoEncontradoException.class, () -> service.actualizarPorVencimiento(1, request));
    }

    @Test
    void lanzaEstadoNoEncontrado() {
        Compromiso compromiso = new Compromiso();
        compromiso.setEstado(new EstadoCompromiso());
        CompromisoActualizacionVencimientoRequest request = new CompromisoActualizacionVencimientoRequest();
        request.setEstado("X");
        request.setRecargoAplicado(0);

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(estadoCompromisoRepository.findByNombre("X")).thenReturn(Optional.empty());

        assertThrows(EstadoNoEncontradoException.class, () -> service.actualizarPorVencimiento(1, request));
    }
}
