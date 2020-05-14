package com.tjf.sample.github.messaging.test.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StarShipExchangeTest {

	String OUTPUT = "starship-test-output";

	@Output(StarShipExchangeTest.OUTPUT)
	MessageChannel output();

	String INPUT = "starship-test-input";

	@Input(StarShipExchangeTest.INPUT)
	SubscribableChannel input();
}