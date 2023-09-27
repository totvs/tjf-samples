package com.tjf.sample.github.messagingstream.infrastructure.messaging;

import com.totvs.tjf.messaging.context.TOTVSMessage;
import com.totvs.tjf.messaging.context.TOTVSMessageBuilder;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class BBUnitPublisher {
	private StreamBridge streamBridge;

	public BBUnitPublisher(StreamBridge streamBridge) {
		this.streamBridge = streamBridge;
	}

	public <T> void publish(T event, String eventName) {
		TOTVSMessage<T> message = TOTVSMessageBuilder.<T>withType(eventName)
			.setContent(event)
			.build();

		message.sendTo(streamBridge, "bbunit-input-out-0");
	}
}
