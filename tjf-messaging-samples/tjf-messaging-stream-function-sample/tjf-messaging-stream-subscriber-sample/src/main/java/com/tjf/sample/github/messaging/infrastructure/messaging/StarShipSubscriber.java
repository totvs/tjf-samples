package com.tjf.sample.github.messaging.infrastructure.messaging;

import com.totvs.tjf.messaging.context.function.ConsumerWithTenant;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipArrivedEventWT;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEventWT;
import com.tjf.sample.github.messaging.model.StarShip;
import com.tjf.sample.github.messaging.services.StarShipService;
import com.totvs.tjf.messaging.TransactionContext;
import com.totvs.tjf.messaging.context.TOTVSMessage;
import com.totvs.tjf.messaging.context.function.FunctionWithoutTenant;

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
	public ConsumerWithTenant<TOTVSMessage<StarShipLeftEvent>> StarShipLeftEvent() {
		return message -> {
			System.out.println("StarShipLeftEvent recebido!");

			StarShipLeftEvent starShipLeftEvent = message.getContent();
			starShipService.left(new StarShip(starShipLeftEvent.getName()));
		};
	}

	@Bean
	public FunctionWithoutTenant<TOTVSMessage<StarShipLeftEventWT>, String> StarShipLeftEventWT() {
		return message -> {
			System.out.println("StarShipLeftEventWT recebido!");

			StarShipLeftEventWT starShipLeftEventWT = message.getContent();
			starShipService.left(new StarShip(starShipLeftEventWT.getName()));
			return "StarShipLeftEventWT recebido!";
		};
	}

	@Bean
	public FunctionWithoutTenant<TOTVSMessage<StarShipArrivedEventWT>, String> StarShipArrivedEventWT() {
		return message -> {
			System.out.println("StarShipArrivedEvent recebido!");

			StarShipArrivedEventWT starShipArrivedEvent = message.getContent();
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
			return null;

		};
	}

	@Bean
	public ConsumerWithTenant<TOTVSMessage<StarShipArrivedEvent>> StarShipArrivedEvent() {
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