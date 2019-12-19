package br.com.star.wars.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;

@Async
@EnableBinding(SGDPKafkaReader.class)
public class Subscriber {
	
	@StreamListener(target = SGDPKafkaReader.INPUT)
	public void listener(Message<?> message) {
		System.out.println("#################### MENSAGEM ENVIADA ######################");
		System.out.println(message);
	}
	
}
