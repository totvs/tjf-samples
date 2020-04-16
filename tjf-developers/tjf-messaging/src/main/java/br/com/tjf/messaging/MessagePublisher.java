package br.com.tjf.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.totvs.tjf.core.message.TOTVSMessage;

@EnableBinding(MessageExchange.class)
public class MessagePublisher {

	MessageExchange exchange;

	public MessagePublisher(MessageExchange exchange) {
		this.exchange = exchange;
	}

	public <T> void publish(T event, String eventName) {
		new TOTVSMessage<T>(eventName, event).sendTo(exchange.output());
	}

}
