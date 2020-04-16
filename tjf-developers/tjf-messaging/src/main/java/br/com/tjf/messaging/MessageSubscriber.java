package br.com.tjf.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.core.message.TOTVSMessage;

import br.com.tjf.events.MessageEvent;

@EnableBinding(MessageExchange.class)
public class MessageSubscriber {

	@StreamListener(target = MessageExchange.INPUT, condition = MessageEvent.CONDITIONAL_EXPRESSION)
	public void subscriberMessage(TOTVSMessage<MessageEvent> message) {
		System.out.println("================");
		System.out.println(message.getContent().getName()); // Conteúdo do evento
		System.out.println(message.getHeader().getTenantId()); // TenantId
		System.out.println("================");
	}

}
