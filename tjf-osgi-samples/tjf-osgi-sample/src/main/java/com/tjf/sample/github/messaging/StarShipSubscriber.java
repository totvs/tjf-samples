package com.tjf.sample.github.messaging;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.totvs.tjf.messaging.context.TOTVSMessage;

@Service
public class StarShipSubscriber {

	@Bean
	public Consumer<TOTVSMessage<StarShipArrivedEvent>> subscribeArrived() {
		return message -> {
			StarShipArrivedEvent starShipArrivedEvent = message.getContent();
			System.out.println("Starship " + starShipArrivedEvent.getName() + " has arrived !!");
		};
	}
}