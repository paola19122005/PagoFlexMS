package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.dto.request.CompromisoActualizacionRequest;
import com.pagoflex.compromisos.dto.response.CompromisoResponse;
import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.exception.CompromisoNoEncontradoException;
import com.pagoflex.compromisos.mapper.CompromisoMapper;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompromisoActualizacionServiceTest {

    @Mock
    private CompromisoRepository compromisoRepository;
    @Mock
    private CompromisoMapper compromisoMapper;

    @InjectMocks
    private CompromisoActualizacionService service;

    @Test
    void actualiza() {
        Compromiso compromiso = new Compromiso();
        CompromisoActualizacionRequest request = new CompromisoActualizacionRequest();
        request.setConcepto("C");
        request.setMonto(10);
        request.setFechaVencimiento(LocalDate.of(2026, 1, 1));

        when(compromisoRepository.findById(1)).thenReturn(Optional.of(compromiso));
        when(compromisoRepository.save(compromiso)).thenReturn(compromiso);
        when(compromisoMapper.toResponse(compromiso)).thenReturn(new CompromisoResponse());

        service.actualizar(1, request);

        org.mockito.Mockito.verify(compromisoRepository).save(compromiso);
    }

    @Test
    void notFound() {
        when(compromisoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(CompromisoNoEncontradoException.class, () -> service.actualizar(1, new CompromisoActualizacionRequest()));
    }
}
