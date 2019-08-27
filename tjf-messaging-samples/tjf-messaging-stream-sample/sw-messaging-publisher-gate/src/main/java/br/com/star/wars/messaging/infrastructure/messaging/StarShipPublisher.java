package br.com.star.wars.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.totvs.tjf.core.message.TOTVSMessage;

@EnableBinding(StarShipExchange.class)
public class StarShipPublisher {

	StarShipExchange exchange;

	public StarShipPublisher(StarShipExchange exchange) {
		this.exchange = exchange;
	}

	public <T> void publish(T event, String eventName) {

		new TOTVSMessage<T>(eventName, event).sendTo(exchange.output());
	}
}