package com.tjf.sample.github.messaging.infrastructure.messaging;

import static com.tjf.sample.github.messaging.infrastructure.messaging.StarShipExchange.INPUT;
import static io.cloudevents.core.CloudEventUtils.mapData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.model.StarShip;
import com.tjf.sample.github.messaging.services.StarShipService;
import com.totvs.tjf.core.common.security.SecurityDetails;
import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.core.message.cloudevent.CloudEventJson;
import com.totvs.tjf.messaging.TransactionContext;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.data.PojoCloudEventData;
import io.cloudevents.jackson.PojoCloudEventDataMapper;

@EnableBinding(StarShipExchange.class)
public class StarShipSubscriber {

	private StarShipService starShipService;

	@Autowired
	private TransactionContext transactionContext;

	@Autowired
	private ObjectMapper objectMapper;

	public StarShipSubscriber(StarShipService starShipService) {
		this.starShipService = starShipService;
	}

	@StreamListener(target = INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION)
	public void subscribeArrived(TOTVSMessage<StarShipArrivedEvent> message) {
		if (transactionContext.getTransactionInfo() != null) {
			System.out.println(
					"TransactionInfo TransactionId: " + transactionContext.getTransactionInfo().getTransactionId());
			System.out.println(
					"TransactionInfo GeneratedBy: " + transactionContext.getTransactionInfo().getGeneratedBy());
		}
		System.out.println("Current tenant: " + SecurityDetails.getTenant());

		StarShipArrivedEvent starShipArrivedEvent = message.getContent();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));
	}

	@StreamListener(target = INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION_CLOUDEVENT)
	public void subscribeArrived(CloudEventJson<StarShipArrivedEvent> event) {
		if (transactionContext.getTransactionInfo() != null) {
			System.out.println("TransactionInfo TaskId: " + transactionContext.getTransactionInfo().getTaskId());
		}
		System.out.println("Current tenant: " + SecurityDetails.getTenant());

		StarShipArrivedEvent starShipArrivedEvent = event.getData();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));
	}

	@StreamListener(target = INPUT, condition = StarShipLeftEvent.CONDITIONAL_EXPRESSION)
	public void subscribeLeft(TOTVSMessage<StarShipLeftEvent> message) {

		StarShipLeftEvent starShipLeftEvent = message.getContent();
		starShipService.left(new StarShip(starShipLeftEvent.getName()));
	}
}
