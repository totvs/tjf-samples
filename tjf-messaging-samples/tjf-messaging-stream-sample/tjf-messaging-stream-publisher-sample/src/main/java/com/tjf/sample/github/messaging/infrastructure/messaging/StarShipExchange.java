package com.tjf.sample.github.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface StarShipExchange {

	String OUTPUT = "starship-output";

	@Output(StarShipExchange.OUTPUT)
	MessageChannel output();

}