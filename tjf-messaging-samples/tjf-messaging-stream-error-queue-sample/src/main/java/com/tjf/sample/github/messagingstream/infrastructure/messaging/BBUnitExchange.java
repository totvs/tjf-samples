package com.tjf.sample.github.messagingstream.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface BBUnitExchange {
	String INPUT = "bbunit-input";

	@Input(BBUnitExchange.INPUT)
	SubscribableChannel input();

	String OUTPUT = "bbunit-output";

	@Output(BBUnitExchange.OUTPUT)
	MessageChannel output();
}
