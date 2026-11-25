
package com.pagoflex.empresas.data;

import com.pagoflex.empresas.entity.Empresas;
import com.pagoflex.empresas.repository.EmpresasRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class EmpresaSeederTest {

    private EmpresasRepository empresasRepository;
    private EmpresaSeeder empresaSeeder;

    @BeforeEach
    void setUp() {
        empresasRepository = mock(EmpresasRepository.class);
        empresaSeeder = new EmpresaSeeder(empresasRepository);
    }

    @Test
    void noCargaDatosCuandoYaExistenEmpresas() throws Exception {
        when(empresasRepository.count()).thenReturn(2L);
        ReflectionTestUtils.setField(empresaSeeder, "seedData", true);

        empresaSeeder.run();

        verify(empresasRepository, never()).saveAll(anyList());
    }

    @Test
    void noCargaDatosCuandoSeedDataEstaDesactivado() throws Exception {
        when(empresasRepository.count()).thenReturn(0L);
        ReflectionTestUtils.setField(empresaSeeder, "seedData", false);

        empresaSeeder.run();

        verify(empresasRepository, never()).saveAll(anyList());
    }

    @Test
    void cargaEmpresasCuandoLaBaseEstaVaciaYSeedDataEstaActivado() throws Exception {
        when(empresasRepository.count()).thenReturn(0L);
        ReflectionTestUtils.setField(empresaSeeder, "seedData", true);

        empresaSeeder.run();

        verify(empresasRepository, times(1)).saveAll(argThat(empresas ->
                ((java.util.Collection<?>) empresas).size() == 3
        ));
    }

    @Test
    void noLanzaExcepcionCuandoLaBaseYaTieneEmpresas() {
        when(empresasRepository.count()).thenReturn(1L);
        ReflectionTestUtils.setField(empresaSeeder, "seedData", true);

        assertDoesNotThrow(() -> empresaSeeder.run());
    }
}
