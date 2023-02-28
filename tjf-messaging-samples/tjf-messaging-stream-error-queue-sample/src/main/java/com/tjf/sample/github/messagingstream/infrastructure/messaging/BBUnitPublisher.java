package com.tjf.sample.github.messagingstream.infrastructure.messaging;

import com.totvs.tjf.messaging.context.TOTVSMessage;
import com.totvs.tjf.messaging.context.TOTVSMessageBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;

@EnableBinding(BBUnitExchange.class)
public class BBUnitPublisher {
	private BBUnitExchange exchange;

	public BBUnitPublisher(BBUnitExchange exchange) {
		this.exchange = exchange;
	}

	public <T> void publish(T event, String eventName) {
		TOTVSMessage<T> message = TOTVSMessageBuilder.<T>withType(eventName)
			.setContent(event)
			.build();

		message.sendTo(exchange.output());
	}
}
