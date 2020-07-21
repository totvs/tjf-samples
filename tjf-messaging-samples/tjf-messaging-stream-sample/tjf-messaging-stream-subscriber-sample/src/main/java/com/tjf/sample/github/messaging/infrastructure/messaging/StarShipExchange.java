package com.tjf.sample.github.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StarShipExchange {

	String INPUT = "starship-input";

	@Input(StarShipExchange.INPUT)
	SubscribableChannel input();
}