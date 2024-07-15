package com.tjf.sample.github.messaging.test.infrastructure.messaging;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipArrivedEventWT;
import com.tjf.sample.github.messaging.events.StarShipArrivedItEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.model.StarShip;
import com.tjf.sample.github.messaging.test.services.StarShipService;
import com.totvs.tjf.core.validation.ValidatorService;
import com.totvs.tjf.messaging.context.TOTVSMessage;

@Configuration
public class StarShipSubscriber {

	@Autowired
	StarShipService starShipService;

	@Autowired
	private ValidatorService validator;

	@Bean("StarShipArrivedEvent")
	public Consumer<TOTVSMessage<StarShipArrivedEvent>> subscribeArrived() {
		return message -> {
			StarShipArrivedEvent starShipArrivedEvent = message.getContent();
			starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));
		};
	}

	@Bean("StarShipArrivedEventWT")
	public Consumer<TOTVSMessage<StarShipArrivedEventWT>> subscribeArrivedWithoutTenant() {
		return message -> {
			StarShipArrivedEventWT starShipArrivedEvent = message.getContent();
			starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));
		};
	}
	
	@Bean("StarShipArrivedItEvent")
	public Consumer<TOTVSMessage<StarShipArrivedItEvent>> subscribeStarShipArrivedItEvent() {
		return message -> {
			StarShipArrivedItEvent starShipArrivedEvent = message.getContent();
			starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));
		};
	}
	
	@Bean("StarShipLeftEvent")
	public Consumer<TOTVSMessage<StarShipLeftEvent>> subscribeLeft() {
		return message -> {
			StarShipLeftEvent starShipLeftEvent = message.getContent();

			validator.validate(starShipLeftEvent).ifPresent(violations -> {
				throw new NullPointerException("Erro do Teste do Messaging");
			});

			starShipService.left(new StarShip(starShipLeftEvent.getName()));
		};
	}
}