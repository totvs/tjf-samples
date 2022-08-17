package com.tjf.sample.github.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.totvs.tjf.messaging.WithoutTenant;

@SpringBootApplication
public class SubscriberApplication {
	@WithoutTenant
	public static void main(String[] args) {
		SpringApplication.run(SubscriberApplication.class, args);
	}
}
