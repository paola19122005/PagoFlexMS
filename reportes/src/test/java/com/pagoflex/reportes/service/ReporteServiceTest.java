package com.pagoflex.reportes.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pagoflex.reportes.dto.request.ReporteEstadoRequest;
import com.pagoflex.reportes.dto.request.ReporteRegistroRequest;
import com.pagoflex.reportes.dto.response.ReporteResponse;
import com.pagoflex.reportes.entity.Reporte;
import com.pagoflex.reportes.entity.ReporteMensaje;
import com.pagoflex.reportes.exception.ReporteNoEncontradoException;
import com.pagoflex.reportes.mapper.ReporteMapper;
import com.pagoflex.reportes.repository.ReporteMensajeRepository;
import com.pagoflex.reportes.repository.ReporteRepository;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock
    private ReporteMensajeRepository mensajeRepository;

    @Mock
    private ReporteMapper reporteMapper;

    @InjectMocks
    private ReporteService service;

    @Test
    void crear() {

        ReporteRegistroRequest request =
                new ReporteRegistroRequest();

        request.setDescripcion("Pago pendiente");
        request.setIdUsuario(5L);
        request.setFecha("22-11-2026");

        Reporte reporte = new Reporte();

        Reporte guardado = new Reporte();
        guardado.setId(1L);
        guardado.setDescripcion("Pago pendiente");
        guardado.setIdUsuario(5L);
        guardado.setFecha("22-11-2026");

        ReporteResponse response =
                new ReporteResponse();

        when(reporteMapper.toEntity(request))
                .thenReturn(reporte);

        when(reporteRepository.save(reporte))
                .thenReturn(guardado);

        when(reporteMapper.toResponse(guardado))
                .thenReturn(response);

        assertEquals(
                response,
                service.crear(request)
        );

        verify(mensajeRepository)
                .save(any(ReporteMensaje.class));
    }

    @Test
    void crearGeneraMensajeInicial() {

        ReporteRegistroRequest request =
                new ReporteRegistroRequest();

        request.setDescripcion("No aparece mi pago");
        request.setIdUsuario(7L);
        request.setFecha("22-11-2026");

        Reporte reporte = new Reporte();

        Reporte guardado = new Reporte();
        guardado.setId(10L);
        guardado.setDescripcion("No aparece mi pago");
        guardado.setIdUsuario(7L);
        guardado.setFecha("22-11-2026");

        when(reporteMapper.toEntity(request))
                .thenReturn(reporte);

        when(reporteRepository.save(reporte))
                .thenReturn(guardado);

        when(reporteMapper.toResponse(guardado))
                .thenReturn(new ReporteResponse());

        service.crear(request);

        ArgumentCaptor<ReporteMensaje> captor =
                ArgumentCaptor.forClass(
                        ReporteMensaje.class
                );

        verify(mensajeRepository)
                .save(captor.capture());

        ReporteMensaje mensaje =
                captor.getValue();

        assertEquals(10L, mensaje.getIdReporte());
        assertEquals(7L, mensaje.getIdRemitente());
        assertEquals("USUARIO", mensaje.getRolRemitente());
        assertEquals(
                "No aparece mi pago",
                mensaje.getContenido()
        );
        assertEquals(
                "22-11-2026",
                mensaje.getFechaHora()
        );
    }

    @Test
    void obtenerPorIdEncontrado() {

        Reporte reporte = new Reporte();
        ReporteResponse response =
                new ReporteResponse();

        when(reporteRepository.findById(1L))
                .thenReturn(Optional.of(reporte));

        when(reporteMapper.toResponse(reporte))
                .thenReturn(response);

        assertEquals(
                response,
                service.obtenerPorId(1L)
        );
    }

    @Test
    void obtenerPorIdNoEncontrado() {

        when(reporteRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ReporteNoEncontradoException.class,
                () -> service.obtenerPorId(1L)
        );
    }

    @Test
    void listarTodos() {

        Reporte reporte = new Reporte();

        when(
                reporteRepository
                        .findAllByOrderByIdDesc()
        ).thenReturn(List.of(reporte));

        when(reporteMapper.toResponse(any()))
                .thenReturn(new ReporteResponse());

        assertEquals(
                1,
                service.listarTodos().size()
        );
    }

    @Test
    void listarPorUsuario() {

        Reporte reporte = new Reporte();

        when(
                reporteRepository
                        .findByIdUsuarioOrderByIdDesc(5L)
        ).thenReturn(List.of(reporte));

        when(reporteMapper.toResponse(any()))
                .thenReturn(new ReporteResponse());

        assertEquals(
                1,
                service.listarPorUsuario(5L).size()
        );
    }

    @Test
    void actualizarEstadoEncontrado() {

        Reporte reporte = new Reporte();
        Reporte guardado = new Reporte();

        ReporteResponse response =
                new ReporteResponse();

        ReporteEstadoRequest request =
                new ReporteEstadoRequest();

        request.setEstado("FINALIZADO");

        when(reporteRepository.findById(1L))
                .thenReturn(Optional.of(reporte));

        when(reporteRepository.save(reporte))
                .thenReturn(guardado);

        when(reporteMapper.toResponse(guardado))
                .thenReturn(response);

        assertEquals(
                response,
                service.actualizarEstado(1L, request)
        );

        assertEquals(
                "FINALIZADO",
                reporte.getEstado()
        );
    }

    @Test
    void actualizarEstadoNoEncontrado() {

        ReporteEstadoRequest request =
                new ReporteEstadoRequest();

        request.setEstado("FINALIZADO");

        when(reporteRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ReporteNoEncontradoException.class,
                () ->
                        service.actualizarEstado(
                                1L,
                                request
                        )
        );
    }

    @Test
    void actualizarEstadoInvalido() {

        Reporte reporte = new Reporte();

        ReporteEstadoRequest request =
                new ReporteEstadoRequest();

        request.setEstado("CERRADITO");

        when(reporteRepository.findById(1L))
                .thenReturn(Optional.of(reporte));

        assertThrows(
                IllegalArgumentException.class,
                () ->
                        service.actualizarEstado(
                                1L,
                                request
                        )
        );

        verify(
                reporteRepository,
                never()
        ).save(any());
    }
}
