package br.com.star.wars.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.messaging.StreamPublisher;

import br.com.star.wars.messaging.model.StarShip;

@EnableBinding(StarShipExchange.class)
public class StarShipPublisher {

	StarShipExchange exchange;

	public StarShipPublisher(StarShipExchange exchange) {
		this.exchange = exchange;
	}

	@StreamPublisher
	public void publish(TOTVSMessage<StarShip> starShip) {
		exchange.output().send(MessageBuilder.withPayload(starShip).setHeader("command", "arrivedStarShip").build());
	}
}