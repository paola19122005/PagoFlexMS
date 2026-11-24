package com.pagoflex.pagos;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PagosApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
    void mainEjecutaSpringApplication() {
        String[] argumentos = {"--spring.main.web-application-type=none"};

        try (MockedStatic<SpringApplication> springApplication =
                     mockStatic(SpringApplication.class)) {

            PagosApplication.main(argumentos);

            springApplication.verify(
                    () -> SpringApplication.run(PagosApplication.class, argumentos)
            );
        }
    }
}
