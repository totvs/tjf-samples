package br.com.tjf.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.core.message.TOTVSMessage;

import br.com.tjf.events.MessageEvent;

@EnableBinding(MessageExchange.class)
public class MessageSubscriber {

	@StreamListener(target = MessageExchange.INPUT, condition = MessageEvent.CONDITIONAL_EXPRESSION)
	public void subscriberMessage(TOTVSMessage<MessageEvent> message) {
		System.out.println("Subscriber da mensagem");
		System.out.println("Conteúdo do evento: " + message.getContent().getName());
		System.out.println("Tenant: " + message.getHeader().getTenantId());
	}

}
