
package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoRegistroRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.entity.EstadoCompromiso;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.exception.ErrorConfiguracionCatalogoException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import com.pagoflex.compromisos.repository.EstadoCompromisoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompromisoRegistroTransaccionalServiceTest {

    @Mock
    private CompromisoRepository compromisoRepository;

    @Mock
    private EstadoCompromisoRepository estadoCompromisoRepository;

    @Mock
    private CompromisoMapper compromisoMapper;

    @InjectMocks
    private CompromisoRegistroTransaccionalService service;

    private CompromisoRegistroRequest crearRequest() {
        CompromisoRegistroRequest request = new CompromisoRegistroRequest();
        request.setIdUsuarioFinal(1);
        request.setIdEmpresa(2);
        request.setConcepto("Pago de prueba");
        request.setMonto(100);
        request.setFechaVencimiento(LocalDate.of(2026, 12, 30));
        return request;
    }

    private EstadoCompromiso crearEstado() {
        EstadoCompromiso estado = new EstadoCompromiso();
        estado.setIdEstadoCompromiso(1);
        estado.setNombre("Pendiente");
        return estado;
    }

    @Test
    void registraCorrectamenteUnCompromiso() {
        CompromisoRegistroRequest request = crearRequest();
        EstadoCompromiso estado = crearEstado();
        Compromiso compromiso = new Compromiso();
        CompromisoResponse response = new CompromisoResponse();

        when(estadoCompromisoRepository.findByNombre("Pendiente"))
                .thenReturn(Optional.of(estado));
        when(compromisoMapper.toEntity(request)).thenReturn(compromiso);
        when(compromisoRepository.count()).thenReturn(0L);
        when(compromisoRepository.existsByFolio(anyString())).thenReturn(false);

        when(compromisoRepository.saveAndFlush(any(Compromiso.class)))
                .thenAnswer(invocation -> {
                    Compromiso guardado = invocation.getArgument(0);
                    guardado.setIdCompromiso(25);
                    return guardado;
                });

        when(compromisoRepository.save(any(Compromiso.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(compromisoMapper.toResponse(any(Compromiso.class)))
                .thenReturn(response);

        CompromisoRegistroResultado resultado = service.registrar(request);

        assertTrue(resultado.creado());
        assertSame(response, resultado.respuesta());
        assertEquals(25, compromiso.getIdCompromiso());
        assertEquals("CP-" + LocalDate.now().getYear() + "-0025",
                compromiso.getFolio());
        assertEquals(estado, compromiso.getEstado());
        assertNotNull(compromiso.getFechaRegistro());
        assertNotNull(compromiso.getFechaCambioEstado());

        verify(compromisoRepository).saveAndFlush(compromiso);
        verify(compromisoRepository).save(compromiso);
    }

    @Test
    void generaOtroFolioCuandoElInicialYaExiste() {
        CompromisoRegistroRequest request = crearRequest();
        Compromiso compromiso = new Compromiso();

        when(estadoCompromisoRepository.findByNombre("Pendiente"))
                .thenReturn(Optional.of(crearEstado()));
        when(compromisoMapper.toEntity(request)).thenReturn(compromiso);
        when(compromisoRepository.count()).thenReturn(0L);
        when(compromisoRepository.existsByFolio(anyString()))
                .thenReturn(true, false);

        when(compromisoRepository.saveAndFlush(any(Compromiso.class)))
                .thenAnswer(invocation -> {
                    Compromiso guardado = invocation.getArgument(0);
                    guardado.setIdCompromiso(30);
                    return guardado;
                });

        when(compromisoRepository.save(any(Compromiso.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(compromisoMapper.toResponse(any(Compromiso.class)))
                .thenReturn(new CompromisoResponse());

        service.registrar(request);

        assertEquals("CP-" + LocalDate.now().getYear() + "-0030",
                compromiso.getFolio());

        verify(compromisoRepository, times(2)).existsByFolio(anyString());
    }

    @Test
    void lanzaErrorCuandoNoExisteEstadoPendiente() {
        CompromisoRegistroRequest request = crearRequest();

        when(estadoCompromisoRepository.findByNombre("Pendiente"))
                .thenReturn(Optional.empty());

        assertThrows(
                ErrorConfiguracionCatalogoException.class,
                () -> service.registrar(request)
        );

        verifyNoInteractions(compromisoMapper);
        verifyNoInteractions(compromisoRepository);
    }

    @Test
    void lanzaErrorCuandoBaseDeDatosNoGeneraIdentificador() {
        CompromisoRegistroRequest request = crearRequest();
        Compromiso compromiso = new Compromiso();

        when(estadoCompromisoRepository.findByNombre("Pendiente"))
                .thenReturn(Optional.of(crearEstado()));
        when(compromisoMapper.toEntity(request)).thenReturn(compromiso);
        when(compromisoRepository.count()).thenReturn(0L);
        when(compromisoRepository.existsByFolio(anyString())).thenReturn(false);
        when(compromisoRepository.saveAndFlush(any(Compromiso.class)))
                .thenReturn(compromiso);

        assertThrows(
                CompromisoPersistenciaException.class,
                () -> service.registrar(request)
        );

        verify(compromisoRepository, never()).save(any(Compromiso.class));
    }

    @Test
    void propagaErrorDeIntegridadDeBaseDeDatos() {
        CompromisoRegistroRequest request = crearRequest();
        Compromiso compromiso = new Compromiso();

        when(estadoCompromisoRepository.findByNombre("Pendiente"))
                .thenReturn(Optional.of(crearEstado()));
        when(compromisoMapper.toEntity(request)).thenReturn(compromiso);
        when(compromisoRepository.count()).thenReturn(0L);
        when(compromisoRepository.existsByFolio(anyString())).thenReturn(false);

        DataIntegrityViolationException excepcion =
                new DataIntegrityViolationException("Error de integridad");

        when(compromisoRepository.saveAndFlush(any(Compromiso.class)))
                .thenThrow(excepcion);

        DataIntegrityViolationException resultado = assertThrows(
                DataIntegrityViolationException.class,
                () -> service.registrar(request)
        );

        assertSame(excepcion, resultado);
    }
}
