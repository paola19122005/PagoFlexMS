package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.ErrorConfiguracionCatalogoException;
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
class CompromisoActualizacionRevisionServiceTest {

    @Mock
    private CompromisoRepository compromisoRepository;
    @Mock
    private EstadoCompromisoRepository estadoCompromisoRepository;
    @Mock
    private CompromisoMapper compromisoMapper;

    @InjectMocks
    private CompromisoActualizacionRevisionService service;

    @Test
    void cambiaARevision() {
        Compromiso compromiso = new Compromiso();
        EstadoCompromiso actual = new EstadoCompromiso();
        actual.setNombre("Pendiente");
        compromiso.setEstado(actual);

        EstadoCompromiso revision = new EstadoCompromiso();
        revision.setNombre("En revisión");

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(estadoCompromisoRepository.findByNombre("En revisión")).thenReturn(Optional.of(revision));
        when(compromisoRepository.save(compromiso)).thenReturn(compromiso);
        when(compromisoMapper.toResponse(compromiso)).thenReturn(new CompromisoResponse());

        service.actualizarRevision(1);

        assertEquals("En revisión", compromiso.getEstado().getNombre());
    }

    @Test
    void noCambiaFechaSiYaEstaEnRevision() {
        Compromiso compromiso = new Compromiso();
        EstadoCompromiso actual = new EstadoCompromiso();
        actual.setNombre("En revisión");
        compromiso.setEstado(actual);

        EstadoCompromiso revision = new EstadoCompromiso();
        revision.setNombre("En revisión");

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(estadoCompromisoRepository.findByNombre("En revisión")).thenReturn(Optional.of(revision));
        when(compromisoRepository.save(compromiso)).thenReturn(compromiso);
        when(compromisoMapper.toResponse(compromiso)).thenReturn(new CompromisoResponse());

        service.actualizarRevision(1);

        assertEquals("En revisión", compromiso.getEstado().getNombre());
    }

    @Test
    void lanzaNotFound() {
        when(compromisoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(CompromisoNoEncontradoException.class, () -> service.actualizarRevision(1));
    }

    @Test
    void lanzaErrorConfiguracion() {
        Compromiso compromiso = new Compromiso();
        compromiso.setEstado(new EstadoCompromiso());
        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(estadoCompromisoRepository.findByNombre("En revisión")).thenReturn(Optional.empty());
        assertThrows(ErrorConfiguracionCatalogoException.class, () -> service.actualizarRevision(1));
    }
}
