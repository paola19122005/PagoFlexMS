package com.pagoflex.reportes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pagoflex.reportes.dto.request.ReporteMensajeRequest;
import com.pagoflex.reportes.dto.response.ReporteMensajeResponse;
import com.pagoflex.reportes.entity.ReporteMensaje;
import com.pagoflex.reportes.exception.ReporteNoEncontradoException;
import com.pagoflex.reportes.mapper.ReporteMensajeMapper;
import com.pagoflex.reportes.repository.ReporteMensajeRepository;
import com.pagoflex.reportes.repository.ReporteRepository;

@ExtendWith(MockitoExtension.class)
class ReporteMensajeTest {

    @Mock
    private ReporteMensajeRepository mensajeRepository;

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private ReporteMensajeMapper mensajeMapper;

    @InjectMocks
    private ReporteMensajeService service;

    @Test
    void listarPorReporteExiste() {

        ReporteMensaje mensaje =
                new ReporteMensaje();

        when(reporteRepository.existsById(1L))
                .thenReturn(true);

        when(
                mensajeRepository
                        .findByIdReporteOrderByIdAsc(1L)
        ).thenReturn(List.of(mensaje));

        when(mensajeMapper.toResponse(any()))
                .thenReturn(
                        new ReporteMensajeResponse()
                );

        assertEquals(
                1,
                service.listarPorReporte(1L).size()
        );
    }

    @Test
    void listarPorReporteNoExiste() {

        when(reporteRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ReporteNoEncontradoException.class,
                () -> service.listarPorReporte(1L)
        );
    }

    @Test
    void registrarExiste() {

        ReporteMensajeRequest request =
                new ReporteMensajeRequest();

        request.setRolRemitente("AGENTE");

        ReporteMensaje mensaje =
                new ReporteMensaje();

        ReporteMensaje guardado =
                new ReporteMensaje();

        ReporteMensajeResponse response =
                new ReporteMensajeResponse();

        when(reporteRepository.existsById(1L))
                .thenReturn(true);

        when(
                mensajeMapper.toEntity(
                        1L,
                        request
                )
        ).thenReturn(mensaje);

        when(mensajeRepository.save(mensaje))
                .thenReturn(guardado);

        when(mensajeMapper.toResponse(guardado))
                .thenReturn(response);

        assertEquals(
                response,
                service.registrar(1L, request)
        );
    }

    @Test
    void registrarNoExiste() {

        ReporteMensajeRequest request =
                new ReporteMensajeRequest();

        when(reporteRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                ReporteNoEncontradoException.class,
                () -> service.registrar(1L, request)
        );
    }

    @Test
    void registrarRolInvalido() {

        ReporteMensajeRequest request =
                new ReporteMensajeRequest();

        request.setRolRemitente("ADMIN");

        when(reporteRepository.existsById(1L))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.registrar(1L, request)
        );

        verify(
                mensajeRepository,
                never()
        ).save(any());
    }

    @Test
    void registrarConvierteRolAMayuscula() {

        ReporteMensajeRequest request =
                new ReporteMensajeRequest();

        request.setRolRemitente("agente");

        ReporteMensaje mensaje =
                new ReporteMensaje();

        ReporteMensaje guardado =
                new ReporteMensaje();

        when(reporteRepository.existsById(1L))
                .thenReturn(true);

        when(
                mensajeMapper.toEntity(
                        1L,
                        request
                )
        ).thenReturn(mensaje);

        when(mensajeRepository.save(mensaje))
                .thenReturn(guardado);

        when(mensajeMapper.toResponse(guardado))
                .thenReturn(
                        new ReporteMensajeResponse()
                );

        service.registrar(1L, request);

        assertEquals(
                "AGENTE",
                request.getRolRemitente()
        );
    }
}
