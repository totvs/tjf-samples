package br.com.star.wars.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.totvs.tjf.core.message.TOTVSMessage;

@EnableBinding(BBUnitExchange.class)
public class BBUnitPublisher {
	BBUnitExchange exchange;

	public BBUnitPublisher(BBUnitExchange exchange) {
		this.exchange = exchange;
	}

	public <T> void publish(T event, String eventName) {
		new TOTVSMessage<T>(eventName, event).sendTo(exchange.output());
	}
}