
package com.pagoflex.pagos.service;

import com.pagoflex.pagos.dto.response.UsuarioRemotoResponse;
import com.pagoflex.pagos.exception.ComunicacionServicioException;
import com.pagoflex.pagos.exception.UsuarioNoEncontradoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioIntegrationServiceTest {

    private RestTemplate restTemplate;
    private UsuarioIntegrationService service;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        service = new UsuarioIntegrationService(
                restTemplate, "http://usuarios/"
        );
    }

    @Test
    void obtenerUsuarioEncontrado() {
        UsuarioRemotoResponse usuario = new UsuarioRemotoResponse();
        usuario.setId(7L);
        usuario.setNombre("Camila Rojas");
        usuario.setActivo(true);

        when(restTemplate.getForObject(
                "http://usuarios/api/usuarios/{id}",
                UsuarioRemotoResponse.class,
                7L
        )).thenReturn(usuario);

        UsuarioRemotoResponse response = service.obtenerUsuario(7L);

        assertSame(usuario, response);

        verify(restTemplate).getForObject(
                "http://usuarios/api/usuarios/{id}",
                UsuarioRemotoResponse.class,
                7L
        );
    }

    @Test
    void obtenerUsuarioRespuestaNula() {
        when(restTemplate.getForObject(
                anyString(), eq(UsuarioRemotoResponse.class), eq(7L)
        )).thenReturn(null);

        assertThrows(
                UsuarioNoEncontradoException.class,
                () -> service.obtenerUsuario(7L)
        );
    }

    @Test
    void obtenerUsuarioError404() {
        when(restTemplate.getForObject(
                anyString(), eq(UsuarioRemotoResponse.class), eq(7L)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        assertThrows(
                UsuarioNoEncontradoException.class,
                () -> service.obtenerUsuario(7L)
        );
    }

    @Test
    void obtenerUsuarioErrorHttp() {
        when(restTemplate.getForObject(
                anyString(), eq(UsuarioRemotoResponse.class), eq(7L)
        )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(
                ComunicacionServicioException.class,
                () -> service.obtenerUsuario(7L)
        );
    }

    @Test
    void obtenerUsuarioErrorComunicacion() {
        when(restTemplate.getForObject(
                anyString(), eq(UsuarioRemotoResponse.class), eq(7L)
        )).thenThrow(new RestClientException("Servicio no disponible"));

        assertThrows(
                ComunicacionServicioException.class,
                () -> service.obtenerUsuario(7L)
        );
    }
}
