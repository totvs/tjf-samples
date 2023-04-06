package com.tjf.sample.github.messaging.amqp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {

	private static final String STARSHIP_EXCHANGE_1 = "starship-exchange-1";
	private static final String STARSHIP_QUEUE_1 = "starship-queue-1";
	private static final String STARSHIP_ROUTING_KEY_1 = "starship-routing-key-1";

	private static final String STARSHIP_EXCHANGE_2 = "starship-exchange-2";
	private static final String STARSHIP_QUEUE_2 = "starship-queue-2";
	private static final String STARSHIP_ROUTING_KEY_2 = "starship-routing-key-2";

	private static final String STARSHIP_EXCHANGE_3 = "starship-exchange-3";
	private static final String STARSHIP_QUEUE_3 = "starship-queue-3";
	private static final String STARSHIP_ROUTING_KEY_3 = "starship-routing-key-3";

	@Bean
	public DirectExchange starship1Exchange() {
		return ExchangeBuilder
			.directExchange(STARSHIP_EXCHANGE_1)
			.build();
	}

	@Bean
	public Queue starship1Queue() {
		return QueueBuilder
			.nonDurable(STARSHIP_QUEUE_1)
			.build();
	}

	@Bean
	public Binding starship1Binding(Queue starship1Queue, DirectExchange starship1Exchange) {
		return BindingBuilder
			.bind(starship1Queue)
			.to(starship1Exchange)
			.with(STARSHIP_ROUTING_KEY_1);
	}

	@Bean
	public DirectExchange starship2Exchange() {
		return ExchangeBuilder
			.directExchange(STARSHIP_EXCHANGE_2)
			.build();
	}

	@Bean
	public Queue starship2Queue() {
		return QueueBuilder
			.nonDurable(STARSHIP_QUEUE_2)
			.build();
	}

	@Bean
	public Binding starship2Binding(Queue starship2Queue, DirectExchange starship2Exchange) {
		return BindingBuilder
			.bind(starship2Queue)
			.to(starship2Exchange)
			.with(STARSHIP_ROUTING_KEY_2);
	}

	@Bean
	public DirectExchange starship3Exchange() {
		return ExchangeBuilder
			.directExchange(STARSHIP_EXCHANGE_3)
			.build();
	}

	@Bean
	public Queue starship3Queue() {
		return QueueBuilder
			.nonDurable(STARSHIP_QUEUE_3)
			.build();
	}

	@Bean
	public Binding starship3Binding(Queue starship3Queue, DirectExchange starship3Exchange) {
		return BindingBuilder
			.bind(starship3Queue)
			.to(starship3Exchange)
			.with(STARSHIP_ROUTING_KEY_3);
	}
}
