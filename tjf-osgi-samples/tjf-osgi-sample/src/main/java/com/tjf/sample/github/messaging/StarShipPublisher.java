package com.tjf.sample.github.messaging;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import com.totvs.tjf.messaging.context.TOTVSMessageBuilder;

@Service
public class StarShipPublisher {

	StreamBridge streamBridge;

	public StarShipPublisher(StreamBridge streamBridge) {
		this.streamBridge = streamBridge;
	}

	public <T> void publish(T event, String eventName) {
		TOTVSMessageBuilder.stream().<T>withType(eventName)
			.setContent(event)
	        .build()
	        .sendTo(streamBridge, "starship-out-0");
	}
}
