package com.tjf.sample.github.messaging.infrastructure.messaging;

import static com.tjf.sample.github.messaging.infrastructure.messaging.StarShipExchange.INPUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.model.StarShip;
import com.tjf.sample.github.messaging.services.StarShipService;
import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.messaging.TransactionContext;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.format.EventFormat;
import io.cloudevents.core.provider.EventFormatProvider;

import io.cloudevents.jackson.JsonFormat;

@EnableBinding(StarShipExchange.class)
public class StarShipSubscriber {

	private StarShipService starShipService;

	@Autowired
	private TransactionContext transactionContext;

	public StarShipSubscriber(StarShipService starShipService) {
		this.starShipService = starShipService;
		
		
		EventFormatProvider.getInstance().registerFormat(new JsonFormat());
	}

	@StreamListener(target = INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION)
	public void subscribeArrived(TOTVSMessage<StarShipArrivedEvent> message) {

		StarShipArrivedEvent starShipArrivedEvent = message.getContent();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));

		if(transactionContext.getTransactionInfo() != null) {
			System.out.println("TransactionInfo TransactionId: "
					+ transactionContext.getTransactionInfo().getTransactionId());
			System.out.println("TransactionInfo GeneratedBy: "
					+ transactionContext.getTransactionInfo().getGeneratedBy());
		}
	}

	@StreamListener(target = INPUT, condition = "headers['type']=='StarShipArrivedCloudEvent'")
	public void subscribeArrived(CloudEvent event) {
		System.out.println(event.getData());
	}

	@StreamListener(target = INPUT, condition = StarShipLeftEvent.CONDITIONAL_EXPRESSION)
	public void subscribeLeft(TOTVSMessage<StarShipLeftEvent> message) {

		StarShipLeftEvent starShipLeftEvent = message.getContent();
		starShipService.left(new StarShip(starShipLeftEvent.getName()));
	}
}
