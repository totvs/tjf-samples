package com.totvs.tjf.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.messaging.interfaces.StarShip;
import com.totvs.tjf.messaging.services.StarShipService;

@EnableBinding(StarShipExchange.class)
public class StarShipSubscriber {

	private StarShipService starShipService;

	public StarShipSubscriber(StarShipService starShipService) {
		this.starShipService = starShipService;
	}

	@StreamListener(target = StarShipExchange.INPUT, condition = "headers['command']=='arrivedStarShip'")
	public void subscribe(StarShip starShip) {
		starShipService.arrived(starShip);
	}
}