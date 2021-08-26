package com.tjf.sample.github.messaging.infrastructure.messaging;

import java.net.URI;
import java.util.UUID;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.core.message.TransactionInfo;

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

	public <T> void publish(T event, String eventName, TransactionInfo transactionInfo) {

		new TOTVSMessage<T>(eventName, event, transactionInfo).sendTo(exchange.output());
	}

}