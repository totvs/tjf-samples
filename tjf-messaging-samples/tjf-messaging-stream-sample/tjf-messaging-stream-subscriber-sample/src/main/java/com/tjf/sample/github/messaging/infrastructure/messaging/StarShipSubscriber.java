package com.tjf.sample.github.messaging.infrastructure.messaging;

import java.util.function.Consumer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Bean;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.model.StarShip;
import com.tjf.sample.github.messaging.services.StarShipService;
import com.totvs.tjf.core.security.context.SecurityDetails;
import com.totvs.tjf.messaging.TransactionContext;
import com.totvs.tjf.messaging.context.TOTVSMessage;

@EnableBinding(StarShipExchange.class)
//@EnableAutoConfiguration
//@Service
public class StarShipSubscriber {

//	@Autowired
	private StarShipService starShipService;
//	@Autowired
	private TransactionContext transactionContext;

	public StarShipSubscriber(StarShipService starShipService, TransactionContext transactionContext) {
		this.starShipService = starShipService;
		this.transactionContext = transactionContext;
}

//	public StarShipSubscriber() {
//		System.out.println("StarShipSubscriber");
//	}

//	@StreamListener(target = INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION)
//	public void subscribeArrived(TOTVSMessage<StarShipArrivedEvent> message) {

	@Bean(name = "subscribearrived")
//	public Consumer<TOTVSMessage<StarShipArrivedEvent>> hire() {
//	public Consumer<String> hire() {
	public Consumer<TOTVSMessage<StarShipArrivedEvent>> subscribeArrived() {
		return  this::subscribeArrived;
	}

	//@StreamListener(target = StarShipExchange.INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION)
	public void subscribeArrived(TOTVSMessage<StarShipArrivedEvent> message) {

		System.out.println("StarShipArrivedEvent recebido");
		//System.out.println(message);			
		
		StarShipArrivedEvent starShipArrivedEvent = message.getContent();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));

		if(transactionContext.getTransactionInfo() != null) {
			System.out.println("TransactionInfo TransactionId: "
					+ transactionContext.getTransactionInfo().getTransactionId());
			System.out.println("TransactionInfo GeneratedBy: "
					+ transactionContext.getTransactionInfo().getGeneratedBy());
		}
	}
	
	//@StreamListener(target = INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION_CLOUDEVENT)
	public void subscribeArrivedCloudEvent(TOTVSMessage<StarShipArrivedEvent> message) {
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

/*
	@StreamListener(target = INPUT, condition = StarShipLeftEvent.CONDITIONAL_EXPRESSION)
	public void subscribeLeft(TOTVSMessage<StarShipLeftEvent> message) {

		StarShipLeftEvent starShipLeftEvent = message.getContent();
		starShipService.left(new StarShip(starShipLeftEvent.getName()));
	}
*/

	@Bean(name = "subscribeleft")
	public Consumer<TOTVSMessage<StarShipLeftEvent>> subscribeLeft() {

		return  message -> {
			
			System.out.println("StarShipLeftEvent recebido");
			
			StarShipLeftEvent starShipLeftEvent = message.getContent();
			starShipService.left(new StarShip(starShipLeftEvent.getName()));
		};
	}
}
