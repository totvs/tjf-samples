package com.totvs.tjf.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.totvs.tjf.messaging.interfaces.StarShip;

@EnableBinding(StarShipExchange.class)
public class StarShipPublisher {

	StarShipExchange exchange;

	public StarShipPublisher(StarShipExchange exchange) {
		this.exchange = exchange;
	}

	public void publish(StarShip starShip) {
		exchange.output().send(MessageBuilder.withPayload(starShip).setHeader("command", "arrivedStarShip").build());
	}
}