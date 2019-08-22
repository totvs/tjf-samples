package br.com.star.wars.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.totvs.tjf.core.message.TOTVSMessage;

import br.com.star.wars.messaging.model.StarShip;

@EnableBinding(StarShipExchange.class)
public class StarShipPublisher {

	StarShipExchange exchange;

	public StarShipPublisher(StarShipExchange exchange) {
		this.exchange = exchange;
	}

	public void publish(StarShip starShip) {
		exchange.output().send(MessageBuilder.withPayload(new TOTVSMessage<StarShip>("arrivedStarShip", starShip))
				.setHeader("type", "arrivedStarShip").build());
	}
}