package com.uniprojecao.fabrica.gprojuridico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class GprojuridicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(GprojuridicoApplication.class, args);
	}
}
