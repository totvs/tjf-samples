package com.totvs.tjf.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.totvs.tjf.messaging.services.StarShipService;

@SpringBootApplication
public class SubscriberApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubscriberApplication.class, args);
	}
	
	@Bean
	StarShipService starShipService() {
		return new StarShipService();
	}
}
