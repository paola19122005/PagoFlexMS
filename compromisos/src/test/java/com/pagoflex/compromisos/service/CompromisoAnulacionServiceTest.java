package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.AnulacionRequest;
import com.pagoflex.compromisos.dto.response.AnulacionResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.entity.MotivoAnulacion;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.CompromisoYaAnuladoException;
import com.pagoflex.compromisos.exception.ErrorConfiguracionCatalogoException;
import com.pagoflex.compromisos.repository.AnulacionRepository;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import com.pagoflex.compromisos.repository.MotivoAnulacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompromisoAnulacionServiceTest {

    @Mock
    private CompromisoRepository compromisoRepository;
    @Mock
    private AnulacionRepository anulacionRepository;
    @Mock
    private MotivoAnulacionRepository motivoAnulacionRepository;
    @Mock
    private EstadoCompromisoRepository estadoCompromisoRepository;

    @InjectMocks
    private CompromisoAnulacionService service;

    @Test
    void anulaConMotivoCatalogo() {
        Compromiso compromiso = new Compromiso();
        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);
        request.setMotivo("Error de registro");

        EstadoCompromiso estadoAnulado = new EstadoCompromiso();
        estadoAnulado.setNombre("Anulado");

        MotivoAnulacion motivo = new MotivoAnulacion();
        motivo.setNombre("Error de registro");

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(anulacionRepository.existsById(1)).thenReturn(false);
        when(estadoCompromisoRepository.findByNombre("Anulado")).thenReturn(Optional.of(estadoAnulado));
        when(motivoAnulacionRepository.findByNombre("Error de registro")).thenReturn(Optional.of(motivo));
        when(anulacionRepository.saveAndFlush(org.mockito.ArgumentMatchers.any())).thenAnswer(invocation -> invocation.getArgument(0));

        AnulacionResponse response = service.anular(1, request);

        assertNotNull(response);
        assertEquals("Anulado", compromiso.getEstado().getNombre());
    }

    @Test
    void lanzaYaAnulado() {
        Compromiso compromiso = new Compromiso();
        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);
        request.setMotivo("Motivo");

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(anulacionRepository.existsById(1)).thenReturn(true);

        assertThrows(CompromisoYaAnuladoException.class, () -> service.anular(1, request));
    }

    @Test
    void lanzaNotFound() {
        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);
        request.setMotivo("Motivo");
        when(compromisoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(CompromisoNoEncontradoException.class, () -> service.anular(1, request));
    }

    @Test
    void lanzaErrorConfiguracion() {
        Compromiso compromiso = new Compromiso();
        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);
        request.setMotivo("Motivo");

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(anulacionRepository.existsById(1)).thenReturn(false);
        when(estadoCompromisoRepository.findByNombre("Anulado")).thenReturn(Optional.empty());

        assertThrows(ErrorConfiguracionCatalogoException.class, () -> service.anular(1, request));
    }
}
