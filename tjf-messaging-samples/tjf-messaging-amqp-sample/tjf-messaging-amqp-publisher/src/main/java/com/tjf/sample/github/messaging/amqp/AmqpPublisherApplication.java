package com.tjf.sample.github.messaging.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AmqpPublisherApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmqpPublisherApplication.class, args);
	}

}
