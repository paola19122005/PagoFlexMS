package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoRegistroRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.exception.OrigenDatosInconsistentesException;
import com.pagoflex.compromisos.exception.ViolacionIntegridadException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompromisoRegistroServiceTest {

    @Mock
    private CompromisoRegistroTransaccionalService transaccionalService;
    @Mock
    private CompromisoOrigenRecuperacionService recuperacionService;
    @Mock
    private CompromisoMapper compromisoMapper;

    @InjectMocks
    private CompromisoRegistroService service;

    @Test
    void registraNuevoSinOrigen() {
        CompromisoRegistroRequest request = new CompromisoRegistroRequest();
        request.setIdUsuarioFinal(1);
        request.setIdEmpresa(2);
        request.setConcepto("Concepto");
        request.setMonto(100);
        request.setFechaVencimiento(LocalDate.of(2026, 1, 1));

        CompromisoResponse response = new CompromisoResponse();
        when(transaccionalService.registrar(request)).thenReturn(new CompromisoRegistroResultado(true, response));

        CompromisoRegistroResultado resultado = service.registrar(request);

        assertEquals(true, resultado.creado());
    }

    @Test
    void recuperaIdempotente() {
        CompromisoRegistroRequest request = new CompromisoRegistroRequest();
        request.setIdUsuarioFinal(1);
        request.setIdEmpresa(2);
        request.setConcepto("Concepto");
        request.setMonto(100);
        request.setFechaVencimiento(LocalDate.of(2026, 1, 1));
        request.setSistemaOrigen("ANDROID_ROOM");
        request.setIdInstanciaOrigen("11111111-2222-3333-4444-555555555555");
        request.setIdCompromisoOrigen(1L);

        Compromiso existente = new Compromiso();
        existente.setIdUsuarioFinal(1);
        existente.setIdEmpresa(2);
        existente.setConcepto("Concepto");
        existente.setMonto(100);
        existente.setFechaVencimiento(LocalDate.of(2026, 1, 1));

        CompromisoResponse response = new CompromisoResponse();
        when(recuperacionService.obtenerPorOrigen("ANDROID_ROOM", "11111111-2222-3333-4444-555555555555", 1L))
                .thenReturn(Optional.of(existente));
        when(compromisoMapper.toResponse(existente)).thenReturn(response);

        CompromisoRegistroResultado resultado = service.registrar(request);

        assertEquals(false, resultado.creado());
    }

    @Test
    void lanzaOrigenDatosInconsistentes() {
        CompromisoRegistroRequest request = new CompromisoRegistroRequest();
        request.setIdUsuarioFinal(1);
        request.setIdEmpresa(2);
        request.setConcepto("Concepto");
        request.setMonto(100);
        request.setFechaVencimiento(LocalDate.of(2026, 1, 1));
        request.setSistemaOrigen("ANDROID_ROOM");
        request.setIdInstanciaOrigen("11111111-2222-3333-4444-555555555555");
        request.setIdCompromisoOrigen(1L);

        Compromiso existente = new Compromiso();
        existente.setIdUsuarioFinal(2);

        when(recuperacionService.obtenerPorOrigen("ANDROID_ROOM", "11111111-2222-3333-4444-555555555555", 1L))
                .thenReturn(Optional.of(existente));

        assertThrows(OrigenDatosInconsistentesException.class, () -> service.registrar(request));
    }

    @Test
    void lanzaCompromisoDuplicado() {
        CompromisoRegistroRequest request = new CompromisoRegistroRequest();
        request.setIdUsuarioFinal(1);
        request.setIdEmpresa(2);
        request.setConcepto("Concepto");
        request.setMonto(100);
        request.setFechaVencimiento(LocalDate.of(2026, 1, 1));

        DataIntegrityViolationException ex = new DataIntegrityViolationException("dup");
        org.hibernate.exception.ConstraintViolationException hibernateEx =
                new org.hibernate.exception.ConstraintViolationException("msg", null, "uk_compromiso_unicidad");
        ex.initCause(hibernateEx);

        when(transaccionalService.registrar(request)).thenThrow(ex);

        assertThrows(ViolacionIntegridadException.class, () -> service.registrar(request));
    }
}
