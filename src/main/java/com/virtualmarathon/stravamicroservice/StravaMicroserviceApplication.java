package com.virtualmarathon.stravamicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class StravaMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StravaMicroserviceApplication.class, args);
	}

}
