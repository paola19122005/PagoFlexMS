
package com.pagoflex.pagos.service;

import com.pagoflex.pagos.dto.request.CompromisoPagoRequest;
import com.pagoflex.pagos.dto.response.CompromisoRemotoResponse;
import com.pagoflex.pagos.exception.ComunicacionServicioException;
import com.pagoflex.pagos.exception.CompromisoNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CompromisoIntegrationServiceTest {

    private RestTemplate restTemplate;
    private CompromisoIntegrationService service;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        service = new CompromisoIntegrationService(
                restTemplate, "http://compromisos/"
        );
    }

    private CompromisoRemotoResponse compromisoValido() {
        CompromisoRemotoResponse compromiso = new CompromisoRemotoResponse();
        compromiso.setIdCompromiso(10L);
        compromiso.setNumeroCuota((short) 2);
        compromiso.setFechaVencimiento(LocalDate.of(2026, 12, 15));
        compromiso.setRecargoAplicado(500);
        compromiso.setEstado("PENDIENTE");
        return compromiso;
    }

    @Test
    void obtenerCompromisoEncontrado() {
        CompromisoRemotoResponse compromiso = compromisoValido();

        when(restTemplate.getForObject(
                "http://compromisos/api/compromisos/{id}",
                CompromisoRemotoResponse.class,
                10L
        )).thenReturn(compromiso);

        CompromisoRemotoResponse resultado = service.obtenerCompromiso(10L);

        assertSame(compromiso, resultado);
        verify(restTemplate).getForObject(
                "http://compromisos/api/compromisos/{id}",
                CompromisoRemotoResponse.class,
                10L
        );
    }

    @Test
    void obtenerCompromisoRespuestaNula() {
        when(restTemplate.getForObject(
                anyString(),
                eq(CompromisoRemotoResponse.class),
                eq(10L)
        )).thenReturn(null);

        assertThrows(
                CompromisoNoEncontradoException.class,
                () -> service.obtenerCompromiso(10L)
        );
    }

    @Test
    void obtenerCompromisoError404() {
        when(restTemplate.getForObject(
                anyString(),
                eq(CompromisoRemotoResponse.class),
                eq(10L)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(
                CompromisoNoEncontradoException.class,
                () -> service.obtenerCompromiso(10L)
        );
    }

    @Test
    void obtenerCompromisoErrorHttp() {
        when(restTemplate.getForObject(
                anyString(),
                eq(CompromisoRemotoResponse.class),
                eq(10L)
        )).thenThrow(new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR
        ));

        assertThrows(
                ComunicacionServicioException.class,
                () -> service.obtenerCompromiso(10L)
        );
    }

    @Test
    void obtenerCompromisoErrorComunicacion() {
        when(restTemplate.getForObject(
                anyString(),
                eq(CompromisoRemotoResponse.class),
                eq(10L)
        )).thenThrow(new RestClientException("Servicio no disponible"));

        assertThrows(
                ComunicacionServicioException.class,
                () -> service.obtenerCompromiso(10L)
        );
    }

    @Test
    void marcarComoPagadoCorrectamente() {
        CompromisoRemotoResponse compromiso = compromisoValido();

        when(restTemplate.exchange(
                eq("http://compromisos/api/compromisos/{id}/pago"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(CompromisoRemotoResponse.class),
                eq(10L)
        )).thenReturn(ResponseEntity.ok(compromiso));

        CompromisoRemotoResponse resultado =
                service.marcarComoPagado(compromiso);

        assertSame(compromiso, resultado);

        verify(restTemplate).exchange(
                eq("http://compromisos/api/compromisos/{id}/pago"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(CompromisoRemotoResponse.class),
                eq(10L)
        );
    }

    @Test
    void marcarComoPagadoUsaValoresPredeterminadosCuandoSonNulos() {
        CompromisoRemotoResponse compromiso = compromisoValido();
        compromiso.setFechaVencimiento(null);
        compromiso.setRecargoAplicado(null);

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(CompromisoRemotoResponse.class),
                eq(10L)
        )).thenAnswer(invocation -> {
            HttpEntity<CompromisoPagoRequest> entity =
                    invocation.getArgument(2);

            CompromisoPagoRequest request = entity.getBody();

            assertNotNull(request);
            assertEquals("PAGADO", request.getEstado());
            assertEquals((short) 2, request.getNumeroCuota());
            assertEquals(LocalDate.now(), request.getFechaVencimiento());
            assertEquals(0, request.getRecargoAplicado());

            return ResponseEntity.ok(compromiso);
        });

        service.marcarComoPagado(compromiso);
    }

    @Test
    void marcarComoPagadoErrorHttp() {
        CompromisoRemotoResponse compromiso = compromisoValido();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(CompromisoRemotoResponse.class),
                eq(10L)
        )).thenThrow(new HttpServerErrorException(
                HttpStatus.INTERNAL_SERVER_ERROR
        ));

        assertThrows(
                ComunicacionServicioException.class,
                () -> service.marcarComoPagado(compromiso)
        );
    }

    @Test
    void marcarComoPagadoErrorComunicacion() {
        CompromisoRemotoResponse compromiso = compromisoValido();

        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(CompromisoRemotoResponse.class),
                eq(10L)
        )).thenThrow(new RestClientException("Servicio no disponible"));

        assertThrows(
                ComunicacionServicioException.class,
                () -> service.marcarComoPagado(compromiso)
        );
    }
}
