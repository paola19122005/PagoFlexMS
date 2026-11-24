package com.pagoflex.empresas;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmpresasApplicationTest {

    @Test
    void contextLoads() {
    }

	@Test
    void mainDebeIniciarLaAplicacion() {
        EmpresasApplication.main(new String[]{});
    }
}