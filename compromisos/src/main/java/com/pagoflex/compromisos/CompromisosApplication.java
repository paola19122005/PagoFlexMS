package com.pagoflex.compromisos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CompromisosApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompromisosApplication.class, args);
	}

}
