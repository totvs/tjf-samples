package com.tjf.sample.github.messaging.infrastructure.messaging;

import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.tjf.sample.github.messaging.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.events.StarShipLeftEvent;
import com.totvs.tjf.messaging.context.TOTVSMessage;

@Component
public class StarShipPublisher {

	@Bean
	public Function<StarShipArrivedEvent, TOTVSMessage<StarShipArrivedEvent>> publishArrivedsssss() {
		return event -> {
			System.out.println(event.getClass().getSimpleName() + " enviado!");

			return new TOTVSMessage<>(StarShipArrivedEvent.NAME, event);
		};
	}

	@Bean
	public Function<StarShipLeftEvent, TOTVSMessage<StarShipLeftEvent>> publishLeftssssss() {
		return event -> {
			System.out.println(event.getClass().getSimpleName() + " enviado!");

			return new TOTVSMessage<>(StarShipArrivedEvent.NAME, event);
		};
	}

	@Bean
	public Supplier<Message<?>> publishLeft() {
		return () -> {
			StarShipLeftEvent event = new StarShipLeftEvent(StarShipLeftEvent.NAME);

			System.out.println(StarShipLeftEvent.NAME + " enviado!");

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			return MessageBuilder.withPayload(new TOTVSMessage<>(StarShipLeftEvent.NAME, "10", event))
					.setHeader("type", "StarShipArrivedEvent").build();
		};
	}
}