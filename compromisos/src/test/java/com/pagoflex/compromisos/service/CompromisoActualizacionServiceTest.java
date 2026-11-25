
package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoActualizacionRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.exception.CompromisoPersistenciaException;
import com.pagoflex.compromisos.exception.ViolacionIntegridadException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompromisoActualizacionServiceTest {

    @Mock
    private CompromisoRepository compromisoRepository;

    @Mock
    private CompromisoMapper compromisoMapper;

    @InjectMocks
    private CompromisoActualizacionService service;

    private CompromisoActualizacionRequest crearRequest() {
        CompromisoActualizacionRequest request =
                new CompromisoActualizacionRequest();

        request.setConcepto("Concepto actualizado");
        request.setMonto(100);
        request.setFechaVencimiento(LocalDate.of(2026, 12, 30));

        return request;
    }

    @Test
    void actualizaCorrectamente() {
        Compromiso compromiso = new Compromiso();
        CompromisoActualizacionRequest request = crearRequest();
        CompromisoResponse response = new CompromisoResponse();

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));
        when(compromisoRepository.save(compromiso))
                .thenReturn(compromiso);
        when(compromisoMapper.toResponse(compromiso))
                .thenReturn(response);

        CompromisoResponse resultado = service.actualizar(1, request);

        assertSame(response, resultado);

        verify(compromisoMapper).actualizarEntidad(compromiso, request);
        verify(compromisoRepository).save(compromiso);
        verify(compromisoMapper).toResponse(compromiso);
    }

    @Test
    void notFound() {
        when(compromisoRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CompromisoNoEncontradoException.class,
                () -> service.actualizar(1, crearRequest())
        );

        verify(compromisoRepository, never()).save(any());
        verifyNoInteractions(compromisoMapper);
    }


    @Test
    void lanzaViolacionIntegridadCuandoElFolioEstaDuplicado() {
        Compromiso compromiso = new Compromiso();
        compromiso.setFolio("CP-2026-0001");

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));

        ConstraintViolationException causa =
                new ConstraintViolationException(
                        "Folio duplicado",
                        null,
                        "uk_compromiso_folio"
                );

        when(compromisoRepository.save(compromiso))
                .thenThrow(new DataIntegrityViolationException(
                        "Error de integridad", causa
                ));

        ViolacionIntegridadException ex = assertThrows(
                ViolacionIntegridadException.class,
                () -> service.actualizar(1, crearRequest())
        );

        assertEquals("FOLIO_DUPLICADO", ex.getCode());
    }


    @Test
    void lanzaViolacionIntegridadCuandoElCompromisoEstaDuplicado() {
        Compromiso compromiso = new Compromiso();

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));

        ConstraintViolationException causa =
                new ConstraintViolationException(
                        "Compromiso duplicado",
                        null,
                        "uk_compromiso_unicidad"
                );

        when(compromisoRepository.save(compromiso))
                .thenThrow(new DataIntegrityViolationException(
                        "Error de integridad", causa
                ));

        assertThrows(
                ViolacionIntegridadException.class,
                () -> service.actualizar(1, crearRequest())
        );
    }

    @Test
    void lanzaErrorPersistenciaCuandoLaRestriccionEsDesconocida() {
        Compromiso compromiso = new Compromiso();

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));

        ConstraintViolationException causa =
                new ConstraintViolationException(
                        "Otra restricción",
                        null,
                        "uk_otra_restriccion"
                );

        when(compromisoRepository.save(compromiso))
                .thenThrow(new DataIntegrityViolationException(
                        "Error de integridad", causa
                ));

        assertThrows(
                CompromisoPersistenciaException.class,
                () -> service.actualizar(1, crearRequest())
        );
    }

    @Test
    void lanzaErrorPersistenciaCuandoNoExisteCausaHibernate() {
        Compromiso compromiso = new Compromiso();

        when(compromisoRepository.findById(1))
                .thenReturn(Optional.of(compromiso));

        when(compromisoRepository.save(compromiso))
                .thenThrow(new DataIntegrityViolationException(
                        "Error de integridad"
                ));

        assertThrows(
                CompromisoPersistenciaException.class,
                () -> service.actualizar(1, crearRequest())
        );
    }
}
