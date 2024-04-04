package br.com.tjf.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.totvs.tjf.messaging.context.TOTVSMessage;

@EnableBinding(MessageExchange.class)
public class MessagePublisher {

	MessageExchange exchange;

	public MessagePublisher(MessageExchange exchange) {
		this.exchange = exchange;
	}

	public <T> void publish(T event, String eventName) {
		TOTVSMessage<T> totvsMessage = TOTVSMessageBuilder.stream().withType(eventName)
		          .setContent(event)
		          .setTransactionInfo(transactionInfo)
		       .build();
		  totvsMessage.sendTo(exchange.output());
	}
}
