package br.com.star.wars.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.scheduling.annotation.Async;

import com.totvs.tjf.sgdp.annotations.SGDPCode;

@Async
@EnableBinding(SGDPKafkaReader.class)
@SGDPCode("Recebimento do dado pessoal na fila")
public class Subscriber {
	
	public static int testCount = 0;
		
	@StreamListener(target = SGDPKafkaReader.INPUT)
	public void listener(Message<?> message) {
		System.out.println("#################### MENSAGEM ENVIADA ######################");
		System.out.println(message);
				
		testCount++;
	}
	
}
