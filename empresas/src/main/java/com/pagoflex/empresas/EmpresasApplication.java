package com.pagoflex.empresas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class EmpresasApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpresasApplication.class, args);
	}

}
