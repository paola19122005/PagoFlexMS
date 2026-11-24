package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoActualizacionVencimientoRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.exception.EstadoNoEncontradoException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

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


    @Test
    void lanzaErrorCuandoRequestEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarPorVencimiento(1, null));
    }

    @Test
    void lanzaErrorCuandoEstadoEsNulo() {
        CompromisoActualizacionVencimientoRequest request =
                new CompromisoActualizacionVencimientoRequest();
        request.setRecargoAplicado(0);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarPorVencimiento(1, request));
    }

    @Test
    void lanzaErrorCuandoEstadoEstaVacio() {
        CompromisoActualizacionVencimientoRequest request =
                new CompromisoActualizacionVencimientoRequest();
        request.setEstado("   ");
        request.setRecargoAplicado(0);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarPorVencimiento(1, request));
    }

    @Test
    void lanzaErrorCuandoEstadoSupera30Caracteres() {
        CompromisoActualizacionVencimientoRequest request =
                new CompromisoActualizacionVencimientoRequest();
        request.setEstado("A".repeat(31));
        request.setRecargoAplicado(0);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarPorVencimiento(1, request));
    }

    @Test
    void lanzaErrorCuandoRecargoEsNulo() {
        CompromisoActualizacionVencimientoRequest request =
                new CompromisoActualizacionVencimientoRequest();
        request.setEstado("Vencido");

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarPorVencimiento(1, request));
    }

    @Test
    void lanzaErrorCuandoRecargoEsNegativo() {
        CompromisoActualizacionVencimientoRequest request =
                new CompromisoActualizacionVencimientoRequest();
        request.setEstado("Vencido");
        request.setRecargoAplicado(-1);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizarPorVencimiento(1, request));
    }

    @Test
    void actualizaSinCambiarFechaCuandoEstadoEsElMismo() {
        Compromiso compromiso = new Compromiso();

        EstadoCompromiso estadoActual = new EstadoCompromiso();
        estadoActual.setNombre("Vencido");

        compromiso.setEstado(estadoActual);

        CompromisoActualizacionVencimientoRequest request =
                new CompromisoActualizacionVencimientoRequest();
        request.setEstado("Vencido");
        request.setRecargoAplicado(10);

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));
        when(estadoCompromisoRepository.findByNombre("Vencido"))
                .thenReturn(Optional.of(estadoActual));
        when(compromisoRepository.save(compromiso))
                .thenReturn(compromiso);
        when(compromisoMapper.toResponse(compromiso))
                .thenReturn(new CompromisoResponse());

        service.actualizarPorVencimiento(1, request);

        assertNull(compromiso.getFechaCambioEstado());
        assertEquals(10, compromiso.getRecargoAplicado());
    }

    @Test
    void lanzaErrorCuandoOcurreProblemaAlGuardar() {
        Compromiso compromiso = new Compromiso();

        EstadoCompromiso estadoActual = new EstadoCompromiso();
        estadoActual.setNombre("Pendiente");
        compromiso.setEstado(estadoActual);

        EstadoCompromiso estadoNuevo = new EstadoCompromiso();
        estadoNuevo.setNombre("Vencido");

        CompromisoActualizacionVencimientoRequest request =
                new CompromisoActualizacionVencimientoRequest();
        request.setEstado("Vencido");
        request.setRecargoAplicado(10);

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));
        when(estadoCompromisoRepository.findByNombre("Vencido"))
                .thenReturn(Optional.of(estadoNuevo));
        when(compromisoRepository.save(compromiso))
                .thenThrow(new DataIntegrityViolationException("Error de integridad"));

        assertThrows(CompromisoPersistenciaException.class,
                () -> service.actualizarPorVencimiento(1, request));

        verify(compromisoRepository).save(compromiso);
        verify(compromisoMapper, never()).toResponse(any());
    }

}
