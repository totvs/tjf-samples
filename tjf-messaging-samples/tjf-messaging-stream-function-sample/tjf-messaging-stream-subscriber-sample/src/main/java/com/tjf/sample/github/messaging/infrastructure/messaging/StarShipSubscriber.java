package com.tjf.sample.github.messaging.infrastructure.messaging;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.model.StarShip;
import com.tjf.sample.github.messaging.services.StarShipService;
import com.totvs.tjf.messaging.TransactionContext;
import com.totvs.tjf.messaging.context.TOTVSMessage;

@EnableAutoConfiguration
@Component
public class StarShipSubscriber {

	private StarShipService starShipService;
	private TransactionContext transactionContext;

	public StarShipSubscriber(StarShipService starShipService, TransactionContext transactionContext) {
		this.starShipService = starShipService;
		this.transactionContext = transactionContext;
	}

	@Bean
	public Consumer<Message<StarShipLeftEvent>> StarShipLeftEvent() {
		return message -> {

			System.out.println("StarShipLeftEvent recebido!");

			StarShipLeftEvent starShipLeftEvent = message.getPayload();
			starShipService.left(new StarShip(starShipLeftEvent.getName()));
		};
	}

	//exemplo com function
	@Bean
	public Function<Message<StarShipLeftEvent>, String> subscribeLeft() {
		return message -> {
			System.out.println("StarShipLeftEvent recebido!");

			StarShipLeftEvent starShipLeftEvent = message.getPayload();
			starShipService.left(new StarShip(starShipLeftEvent.getName()));
			
			return "StarShipLeftEvent recebido!";
		};
	}

	@Bean
	public Consumer<TOTVSMessage<StarShipArrivedEvent>> StarShipArrivedEvent() {
		return message -> {
			System.out.println("StarShipArrivedEvent recebido!");

			StarShipArrivedEvent starShipArrivedEvent = message.getContent();
			starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));

			if (transactionContext.getTransactionInfo() != null) {
				System.out.println(
						"TransactionInfo TransactionId: " + transactionContext.getTransactionInfo().getTransactionId());
				System.out.println(
						"TransactionInfo GeneratedBy: " + transactionContext.getTransactionInfo().getGeneratedBy());
			}

			if (message.getHeader().getCloudEventsInfo() != null) {
				System.out.println("CloudEventInfo Id: " + message.getHeader().getCloudEventsInfo().getId());
				System.out
						.println("CloudEventInfo Schema: " + message.getHeader().getCloudEventsInfo().getDataSchema());
				System.out.println("CloudEventInfo DataContentType: "
						+ message.getHeader().getCloudEventsInfo().getDataContentType());
			}

		};
	}
}
