package com.tjf.sample.github.messaging.infrastructure.messaging;

import com.totvs.tjf.messaging.context.TOTVSMessageBuilder;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class StarShipPublisher {

	private final StreamBridge streamBridge;

	public StarShipPublisher(StreamBridge streamBridge) {
		this.streamBridge = streamBridge;
	}

	public <T> void publishEvent(T starShipEvent) {
		System.out.println(starShipEvent.getClass().getSimpleName() + " enviado!");
		var message = TOTVSMessageBuilder
				.withType(starShipEvent.getClass().getSimpleName())
				.setContent(starShipEvent)
				.build();

		streamBridge.send("publishArrived-out-0", message);
	}
}