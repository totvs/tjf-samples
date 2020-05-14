package com.tjf.sample.github.messaging;

import static com.tjf.sample.github.messaging.StarShipExchange.INPUT;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.messaging.WithoutTenant;

@EnableBinding(StarShipExchange.class)
public class StarShipSubscriber {

	@WithoutTenant
	@StreamListener(target = INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION)
	public void subscribeArrived(TOTVSMessage<StarShipArrivedEvent> message) {
		
		StarShipArrivedEvent starShipArrivedEvent = message.getContent();
		System.out.println("Starship " + starShipArrivedEvent.getName() + " has arrived !!");
		
	}
	
}