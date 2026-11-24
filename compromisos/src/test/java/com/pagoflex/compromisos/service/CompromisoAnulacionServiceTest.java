package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.AnulacionRequest;
import com.pagoflex.compromisos.dto.response.AnulacionResponse;
import com.pagoflex.compromisos.entity.Anulacion;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.entity.MotivoAnulacion;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.CompromisoYaAnuladoException;
import com.pagoflex.compromisos.exception.ErrorConfiguracionCatalogoException;
import com.pagoflex.compromisos.exception.MotivoAnulacionNoEncontradoException;
import com.pagoflex.compromisos.repository.AnulacionRepository;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import com.pagoflex.compromisos.repository.MotivoAnulacionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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


    @Test
    void lanzaErrorCuandoRequestEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> service.anular(1, null));
    }

    @Test
    void lanzaErrorCuandoIdEjecutivoEsNulo() {
        AnulacionRequest request = new AnulacionRequest();
        request.setMotivo("Motivo");

        assertThrows(IllegalArgumentException.class,
                () -> service.anular(1, request));
    }

    @Test
    void lanzaErrorCuandoIdEjecutivoEsCero() {
        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(0);
        request.setMotivo("Motivo");

        assertThrows(IllegalArgumentException.class,
                () -> service.anular(1, request));
    }

    @Test
    void lanzaErrorCuandoIdEjecutivoEsNegativo() {
        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(-1);
        request.setMotivo("Motivo");

        assertThrows(IllegalArgumentException.class,
                () -> service.anular(1, request));
    }

    @Test
    void lanzaErrorCuandoMotivoEsNulo() {
        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);

        assertThrows(IllegalArgumentException.class,
                () -> service.anular(1, request));
    }

    @Test
    void lanzaErrorCuandoMotivoEstaVacio() {
        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);
        request.setMotivo("   ");

        assertThrows(IllegalArgumentException.class,
                () -> service.anular(1, request));
    }

    @Test
    void lanzaErrorCuandoMotivoSupera255Caracteres() {
        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);
        request.setMotivo("A".repeat(256));

        assertThrows(IllegalArgumentException.class,
                () -> service.anular(1, request));
    }

    @Test
    void anulaConMotivoOtroYGuardaComentario() {
        Compromiso compromiso = new Compromiso();

        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);
        request.setMotivo("Situación especial");

        EstadoCompromiso estadoAnulado = new EstadoCompromiso();
        estadoAnulado.setNombre("Anulado");

        MotivoAnulacion motivoOtro = new MotivoAnulacion();
        motivoOtro.setNombre("Otro");

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));
        when(anulacionRepository.existsById(1)).thenReturn(false);
        when(estadoCompromisoRepository.findByNombre("Anulado"))
                .thenReturn(Optional.of(estadoAnulado));
        when(motivoAnulacionRepository.findByNombre("Situación especial"))
                .thenReturn(Optional.empty());
        when(motivoAnulacionRepository.findByNombre("Otro"))
                .thenReturn(Optional.of(motivoOtro));
        when(anulacionRepository.saveAndFlush(any(Anulacion.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AnulacionResponse response = service.anular(1, request);

        assertNotNull(response);
        assertEquals("Otro", response.getMotivo());
        assertEquals("Situación especial", response.getComentario());
        assertEquals("Anulado", response.getEstadoCompromiso());
    }

    @Test
    void lanzaErrorCuandoNoExisteMotivoOtroEnCatalogo() {
        Compromiso compromiso = new Compromiso();

        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);
        request.setMotivo("Motivo no registrado");

        EstadoCompromiso estadoAnulado = new EstadoCompromiso();
        estadoAnulado.setNombre("Anulado");

        MotivoAnulacion motivoOtro = new MotivoAnulacion();

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));
        when(anulacionRepository.existsById(1)).thenReturn(false);
        when(estadoCompromisoRepository.findByNombre("Anulado"))
                .thenReturn(Optional.of(estadoAnulado));
        when(motivoAnulacionRepository.findByNombre("Motivo no registrado"))
                .thenReturn(Optional.empty());
        when(motivoAnulacionRepository.findByNombre("Otro"))
                .thenReturn(Optional.empty());

        assertThrows(MotivoAnulacionNoEncontradoException.class,
                () -> service.anular(1, request));
    }

    @Test
    void lanzaYaAnuladoCuandoOcurreDuplicidadAlGuardar() {
        Compromiso compromiso = new Compromiso();

        AnulacionRequest request = new AnulacionRequest();
        request.setIdEjecutivo(1);
        request.setMotivo("Error de registro");

        EstadoCompromiso estadoAnulado = new EstadoCompromiso();
        estadoAnulado.setNombre("Anulado");

        MotivoAnulacion motivo = new MotivoAnulacion();
        motivo.setNombre("Error de registro");

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));
        when(anulacionRepository.existsById(1)).thenReturn(false);
        when(estadoCompromisoRepository.findByNombre("Anulado"))
                .thenReturn(Optional.of(estadoAnulado));
        when(motivoAnulacionRepository.findByNombre("Error de registro"))
                .thenReturn(Optional.of(motivo));
        when(anulacionRepository.saveAndFlush(any(Anulacion.class)))
                .thenThrow(new DataIntegrityViolationException("Duplicidad"));

        assertThrows(CompromisoYaAnuladoException.class,
                () -> service.anular(1, request));

        verify(compromisoRepository, never()).save(any(Compromiso.class));
    }

}
