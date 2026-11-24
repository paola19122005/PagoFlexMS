package com.pagoflex.pagos.service;

import com.pagoflex.pagos.dto.request.CompromisoPagoRequest;
import com.pagoflex.pagos.dto.response.CompromisoRemotoResponse;
import com.pagoflex.pagos.exception.ComunicacionServicioException;
import com.pagoflex.pagos.exception.CompromisoNoEncontradoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
public class CompromisoIntegrationService {

    private static final String ESTADO_PAGADO = "PAGADO";

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CompromisoIntegrationService(RestTemplate restTemplate,
                                        @Value("${compromisos.base-url:http://compromisos}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = normalizar(baseUrl);
    }

    public CompromisoRemotoResponse obtenerCompromiso(Long compromisoId) {
        try {
            CompromisoRemotoResponse compromiso = restTemplate.getForObject(
                    baseUrl + "/api/compromisos/{id}", CompromisoRemotoResponse.class, compromisoId);
            if (compromiso == null) {
                throw new CompromisoNoEncontradoException("Compromiso no encontrado: " + compromisoId);
            }
            return compromiso;
        } catch (HttpStatusCodeException ex) {
            if (ex.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                throw new CompromisoNoEncontradoException("Compromiso no encontrado: " + compromisoId);
            }
            throw new ComunicacionServicioException(
                    "No fue posible consultar el compromiso " + compromisoId + " en el microservicio compromisos", ex);
        } catch (RestClientException ex) {
            throw new ComunicacionServicioException(
                    "No fue posible comunicarse con el microservicio compromisos", ex);
        }
    }

    public CompromisoRemotoResponse marcarComoPagado(CompromisoRemotoResponse compromiso) {
        CompromisoPagoRequest request = new CompromisoPagoRequest();
        request.setEstado(ESTADO_PAGADO);
        request.setNumeroCuota(compromiso.getNumeroCuota());
        request.setFechaVencimiento(compromiso.getFechaVencimiento() != null
                ? compromiso.getFechaVencimiento()
                : LocalDate.now());
        request.setRecargoAplicado(compromiso.getRecargoAplicado() != null
                ? compromiso.getRecargoAplicado()
                : 0);

        try {
            ResponseEntity<CompromisoRemotoResponse> response = restTemplate.exchange(
                    baseUrl + "/api/compromisos/{id}/pago",
                    HttpMethod.PUT,
                    new HttpEntity<>(request),
                    CompromisoRemotoResponse.class,
                    compromiso.getIdCompromiso());
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            throw new ComunicacionServicioException(
                    "No fue posible actualizar el compromiso " + compromiso.getIdCompromiso()
                            + " a estado PAGADO", ex);
        } catch (RestClientException ex) {
            throw new ComunicacionServicioException(
                    "No fue posible comunicarse con el microservicio compromisos", ex);
        }
    }

    private String normalizar(String url) {
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
