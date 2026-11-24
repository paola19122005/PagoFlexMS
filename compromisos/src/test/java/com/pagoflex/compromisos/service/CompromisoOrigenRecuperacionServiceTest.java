
package com.pagoflex.compromisos.service;

import com.pagoflex.compromisos.entity.Compromiso;
import com.pagoflex.compromisos.repository.CompromisoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompromisoOrigenRecuperacionServiceTest {

    @Mock
    private CompromisoRepository compromisoRepository;

    @InjectMocks
    private CompromisoOrigenRecuperacionService service;

    @Test
    void obtieneCompromisoCuandoExisteEnOrigen() {
        Compromiso compromiso = new Compromiso();

        when(compromisoRepository
                .findBySistemaOrigenAndIdInstanciaOrigenAndIdCompromisoOrigen(
                        "EMPRESAS", "instancia-123", 10L))
                .thenReturn(Optional.of(compromiso));

        Optional<Compromiso> resultado = service.obtenerPorOrigen(
                "EMPRESAS", "instancia-123", 10L);

        assertTrue(resultado.isPresent());
        assertSame(compromiso, resultado.get());

        verify(compromisoRepository).findBySistemaOrigenAndIdInstanciaOrigenAndIdCompromisoOrigen(
                "EMPRESAS", "instancia-123", 10L);
    }

    @Test
    void retornaVacioCuandoNoExisteCompromisoEnOrigen() {
        when(compromisoRepository
                .findBySistemaOrigenAndIdInstanciaOrigenAndIdCompromisoOrigen(
                        "EMPRESAS", "instancia-123", 10L))
                .thenReturn(Optional.empty());

        Optional<Compromiso> resultado = service.obtenerPorOrigen(
                "EMPRESAS", "instancia-123", 10L);

        assertTrue(resultado.isEmpty());

        verify(compromisoRepository).findBySistemaOrigenAndIdInstanciaOrigenAndIdCompromisoOrigen(
                "EMPRESAS", "instancia-123", 10L);
    }
}
