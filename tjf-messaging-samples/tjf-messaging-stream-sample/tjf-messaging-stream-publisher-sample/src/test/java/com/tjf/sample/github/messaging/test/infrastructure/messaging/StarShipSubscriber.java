package com.tjf.sample.github.messaging.test.infrastructure.messaging;

import static com.tjf.sample.github.messaging.test.infrastructure.messaging.StarShipExchangeTest.INPUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipArrivedWithoutTenantEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.tjf.sample.github.messaging.model.StarShip;
import com.tjf.sample.github.messaging.test.services.StarShipService;
import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.core.validation.ValidatorService;
import com.totvs.tjf.messaging.WithoutTenant;

@EnableBinding(StarShipExchangeTest.class)
public class StarShipSubscriber {

	@Autowired
	StarShipService starShipService;

	@Autowired
	private ValidatorService validator;

	@StreamListener(target = INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION)
	public void subscribeArrived(TOTVSMessage<StarShipArrivedEvent> message) {
		StarShipArrivedEvent starShipArrivedEvent = message.getContent();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));
	}

	@WithoutTenant
	@StreamListener(target = INPUT, condition = StarShipArrivedWithoutTenantEvent.CONDITIONAL_EXPRESSION)
	public void subscribeArrivedWithoutTenant(TOTVSMessage<StarShipArrivedWithoutTenantEvent> message) {
		StarShipArrivedWithoutTenantEvent starShipArrivedEvent = message.getContent();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));
	}

	@WithoutTenant
	@StreamListener(target = INPUT, condition = StarShipLeftEvent.CONDITIONAL_EXPRESSION)
	public void subscribeLeft(TOTVSMessage<StarShipLeftEvent> message) {
		StarShipLeftEvent starShipLeftEvent = message.getContent();

		validator.validate(starShipLeftEvent).ifPresent(violations -> {
			throw new NullPointerException("Erro do Teste do Messaging");
		});

		starShipService.left(new StarShip(starShipLeftEvent.getName()));
	}

}
