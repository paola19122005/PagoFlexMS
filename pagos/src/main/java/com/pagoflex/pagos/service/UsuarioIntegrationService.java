package com.pagoflex.pagos.service;

import com.pagoflex.pagos.dto.response.UsuarioRemotoResponse;
import com.pagoflex.pagos.exception.ComunicacionServicioException;
import com.pagoflex.pagos.exception.UsuarioNoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class UsuarioIntegrationService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public UsuarioIntegrationService(RestTemplate restTemplate,
                                     @Value("${usuarios.base-url:http://usuarios}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = normalizar(baseUrl);
    }

    public UsuarioRemotoResponse obtenerUsuario(Long usuarioId) {
        try {
            UsuarioRemotoResponse usuario = restTemplate.getForObject(
                    baseUrl + "/api/usuarios/{id}", UsuarioRemotoResponse.class, usuarioId);
            if (usuario == null) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado: " + usuarioId);
            }
            return usuario;
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                throw new UsuarioNoEncontradoException("Usuario no encontrado: " + usuarioId);
            }
            throw new ComunicacionServicioException(
                    "No fue posible consultar el usuario " + usuarioId + " en el microservicio usuarios", ex);
        } catch (RestClientException ex) {
            throw new ComunicacionServicioException(
                    "No fue posible comunicarse con el microservicio usuarios", ex);
        }
    }

    private String normalizar(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
