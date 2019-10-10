package br.com.star.wars.messaging.infrastructure.messaging;

import static br.com.star.wars.messaging.infrastructure.messaging.StarShipExchange.INPUT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.core.message.TOTVSMessage;

import br.com.star.wars.messaging.events.StarShipArrivedEvent;
import br.com.star.wars.messaging.events.StarShipLeftEvent;
import br.com.star.wars.messaging.infrastructure.messaging.StarShipExchange;
import br.com.star.wars.messaging.model.StarShip;
import br.com.star.wars.messaging.services.StarShipService;

@EnableBinding(StarShipExchange.class)
public class StarShipSubscriber {
	
	@Autowired
	StarShipService starShipService;

	@StreamListener(target = INPUT, condition = StarShipArrivedEvent.CONDITIONAL_EXPRESSION)
	public void subscribeArrived(TOTVSMessage<StarShipArrivedEvent> message) {
		
		StarShipArrivedEvent starShipArrivedEvent = message.getContent();
		starShipService.arrived(new StarShip(starShipArrivedEvent.getName()));
	}
	
	@StreamListener(target = INPUT, condition = StarShipLeftEvent.CONDITIONAL_EXPRESSION)
	public void subscribeLeft(TOTVSMessage<StarShipLeftEvent> message) {
		
		StarShipLeftEvent starShipLeftEvent = message.getContent();
		starShipService.left(new StarShip(starShipLeftEvent.getName()));
	}
}
