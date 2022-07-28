package com.tjf.sample.github.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.totvs.tjf.messaging.context.TOTVSMessage;

@Component
public class StarShipPublisher {

	private final StreamBridge streamBridge;

	public StarShipPublisher(StreamBridge streamBridge) {
		this.streamBridge = streamBridge;
	}

	public void publishArrivedEvent(StarShipArrivedEvent starShipEvent) {
		System.out.println(starShipEvent.getClass().getSimpleName() + " enviado!");
		
		var message = MessageBuilder.withPayload(new TOTVSMessage<>(StarShipArrivedEvent.NAME, starShipEvent))
				.setHeader("type", "StarShipArrivedEvent").build();

		streamBridge.send("publishArrived-out-0", message);
	}

	public void publishLeftEvent(StarShipLeftEvent starShipEvent) {
		System.out.println(starShipEvent.getClass().getSimpleName() + " enviado!");

		var message = MessageBuilder.withPayload(new TOTVSMessage<>(StarShipLeftEvent.NAME, starShipEvent))
				.setHeader("type", "StarShipLeftEvent").build();

		streamBridge.send("publishLeft-out-0", message);
	}
}