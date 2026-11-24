package com.pagoflex.compromisos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CompromisosApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
    void mainDebeEjecutarLaAplicacion() {
        CompromisosApplication.main(new String[]{});
    }

}
