package com.tjf.sample.github.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.messaging.context.CloudEventsInfo;
import com.totvs.tjf.messaging.context.TOTVSMessage;
import com.totvs.tjf.messaging.context.TransactionInfo;

@EnableBinding(StarShipExchange.class)
public class StarShipPublisher {

	StarShipExchange exchange;
	ObjectMapper mapper;

	public StarShipPublisher(StarShipExchange exchange, ObjectMapper mapper) {
		this.exchange = exchange;
		this.mapper = mapper;
	}

	public <T> void publish(T event, String eventName) {

		new TOTVSMessage<T>(eventName, event).sendTo(exchange.output());
	}
	
	public <T> void publish(T event, String eventName, TransactionInfo transactionInfo, CloudEventsInfo cloudEventsInfo) {

		new TOTVSMessage<T>(eventName, event, transactionInfo, cloudEventsInfo).sendTo(exchange.output());
	}

	public <T> void publish(T event, String eventName, TransactionInfo transactionInfo) {

		new TOTVSMessage<T>(eventName, event, transactionInfo).sendTo(exchange.output());
	}

}