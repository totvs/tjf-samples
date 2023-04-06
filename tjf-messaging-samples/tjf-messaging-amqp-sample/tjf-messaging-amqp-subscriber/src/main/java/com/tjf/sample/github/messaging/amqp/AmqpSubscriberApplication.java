package com.tjf.sample.github.messaging.amqp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AmqpSubscriberApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmqpSubscriberApplication.class, args);
	}

}
