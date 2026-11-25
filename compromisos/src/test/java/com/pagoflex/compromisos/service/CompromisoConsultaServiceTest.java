package com.pagoflex.compromisos.service;

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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompromisoConsultaServiceTest {

    @Mock
    private CompromisoRepository compromisoRepository;
    @Mock
    private EstadoCompromisoRepository estadoCompromisoRepository;
    @Mock
    private CompromisoMapper compromisoMapper;

    @InjectMocks
    private CompromisoConsultaService service;

    @Test
    void obtenerPorId() {
        Compromiso c = new Compromiso();
        when(compromisoRepository.findById(1)).thenReturn(Optional.of(c));
        when(compromisoMapper.toResponse(c)).thenReturn(new CompromisoResponse());
        service.obtenerPorId(1);
    }

    @Test
    void obtenerPorFolio() {
        Compromiso c = new Compromiso();
        when(compromisoRepository.findByFolio("F")).thenReturn(Optional.of(c));
        when(compromisoMapper.toResponse(c)).thenReturn(new CompromisoResponse());
        service.obtenerPorFolio("F");
    }

    @Test
    void listarPorUsuario() {
        when(compromisoRepository.findByIdUsuarioFinal(1)).thenReturn(List.of(new Compromiso()));
        when(compromisoMapper.toResponse(org.mockito.ArgumentMatchers.any())).thenReturn(new CompromisoResponse());
        assertEquals(1, service.listarPorUsuario(1).size());
    }

    @Test
    void listarPorEmpresa() {
        when(compromisoRepository.findByIdEmpresa(1)).thenReturn(List.of(new Compromiso()));
        when(compromisoMapper.toResponse(org.mockito.ArgumentMatchers.any())).thenReturn(new CompromisoResponse());
        assertEquals(1, service.listarPorEmpresa(1).size());
    }

    @Test
    void listarPorUsuarioYEmpresa() {
        when(compromisoRepository.findByIdUsuarioFinalAndIdEmpresa(1, 2)).thenReturn(List.of(new Compromiso()));
        when(compromisoMapper.toResponse(org.mockito.ArgumentMatchers.any())).thenReturn(new CompromisoResponse());
        assertEquals(1, service.listarPorUsuarioYEmpresa(1, 2).size());
    }

    @Test
    void listarPorEstado() {
        EstadoCompromiso estado = new EstadoCompromiso();
        when(estadoCompromisoRepository.findByNombre("Pendiente")).thenReturn(Optional.of(estado));
        when(compromisoRepository.findByEstado(estado)).thenReturn(List.of(new Compromiso()));
        when(compromisoMapper.toResponse(org.mockito.ArgumentMatchers.any())).thenReturn(new CompromisoResponse());
        assertEquals(1, service.listarPorEstado("Pendiente").size());
    }

    @Test
    void listarTodos() {
        when(compromisoRepository.findAll()).thenReturn(List.of(new Compromiso()));
        when(compromisoMapper.toResponse(org.mockito.ArgumentMatchers.any())).thenReturn(new CompromisoResponse());
        assertEquals(1, service.listarTodos().size());
    }

    @Test
    void notFoundId() {
        when(compromisoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(CompromisoNoEncontradoException.class, () -> service.obtenerPorId(1));
    }

    @Test
    void estadoNoEncontrado() {
        when(estadoCompromisoRepository.findByNombre("X")).thenReturn(Optional.empty());
        assertThrows(EstadoNoEncontradoException.class, () -> service.listarPorEstado("X"));
    }
}
