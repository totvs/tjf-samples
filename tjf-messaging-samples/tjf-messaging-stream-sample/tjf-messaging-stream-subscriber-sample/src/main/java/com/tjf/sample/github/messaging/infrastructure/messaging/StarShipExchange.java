package com.tjf.sample.github.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StarShipExchange {

	String INPUT = "starship-input";
	String INPUT_TEMP = "starship-temp";

	@Input(StarShipExchange.INPUT)
	SubscribableChannel input();

	@Input(StarShipExchange.INPUT_TEMP)
	SubscribableChannel inputTemp();
}
