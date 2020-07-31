package com.tjf.sample.github.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.core.message.TransactionInfo;

@EnableBinding(StarShipExchange.class)
public class StarShipPublisher {

	StarShipExchange exchange;

	public StarShipPublisher(StarShipExchange exchange) {
		this.exchange = exchange;
	}

	public <T> void publish(T event, String eventName) {

		new TOTVSMessage<T>(eventName, event).sendTo(exchange.output());
	}

	public <T> void publish(T event, String eventName, TransactionInfo transactionInfo) {

		new TOTVSMessage<T>(eventName, event, transactionInfo).sendTo(exchange.output());
	}
}