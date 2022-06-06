package com.tjf.sample.github.messaging.infrastructure.messaging;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.model.StarShip;
import com.tjf.sample.github.messaging.services.StarShipService;
import com.totvs.tjf.core.security.context.SecurityDetails;
import com.totvs.tjf.messaging.function.ConsumerWithoutTenant;
import com.totvs.tjf.messaging.TransactionContext;
import com.totvs.tjf.messaging.WithoutTenant;
import com.totvs.tjf.messaging.context.TOTVSMessage;

@Service
public class StarShipSubscriber {

	private StarShipService starShipService;
	private TransactionContext transactionContext;

	public StarShipSubscriber(StarShipService starShipService, TransactionContext transactionContext) {
		this.starShipService = starShipService;
		this.transactionContext = transactionContext;
	}

	@Bean
	public Consumer<TOTVSMessage<StarShipArrivedEvent>> subscribeArrived() {
		return this::subscribeArrived;
	}

	//@StreamListener(target = StarShipExchange.INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION)
	public void subscribeArrived(TOTVSMessage<StarShipArrivedEvent> message) {

		System.out.println("StarShipArrivedEvent recebido");

		// # teste fila de erro 
		//if(true)
		//	throw new RuntimeException("testando");
		
		StarShipArrivedEvent starShipArrivedEvent = message.getContent();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));

		if(transactionContext.getTransactionInfo() != null) {
			System.out.println("TransactionInfo TransactionId: "
					+ transactionContext.getTransactionInfo().getTransactionId());
			System.out.println("TransactionInfo GeneratedBy: "
					+ transactionContext.getTransactionInfo().getGeneratedBy());
		}
	}
	
	@Bean
	public Consumer<TOTVSMessage<StarShipLeftEvent>> subscribeLeft() {

		return  message -> {
			
			System.out.println("StarShipLeftEvent recebido");
			
			StarShipLeftEvent starShipLeftEvent = message.getContent();
			starShipService.left(new StarShip(starShipLeftEvent.getName()));
		};
	}

	/* public void subscribeArrivedCloudEvent(TOTVSMessage<StarShipArrivedEvent> message) {
	if (transactionContext.getTransactionInfo() != null) {
		System.out.println("TransactionInfo TaskId: " + transactionContext.getTransactionInfo().getTaskId());
	}
	System.out.println("Current tenant: " + SecurityDetails.getTenant());
	System.out.println("CloudEventInfo Id: " + message.getHeader().getCloudEventsInfo().getId());
	System.out.println("CloudEventInfo Schema: " + message.getHeader().getCloudEventsInfo().getDataSchema());
	System.out.println("CloudEventInfo DataContentType: " + message.getHeader().getCloudEventsInfo().getDataContentType());
	StarShipArrivedEvent starShipArrivedEvent = message.getContent();
	starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));
	} 
	*/
}
