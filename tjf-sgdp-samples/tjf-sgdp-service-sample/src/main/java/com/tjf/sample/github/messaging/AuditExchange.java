package com.tjf.sample.github.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface AuditExchange {

	String INPUT = "sgdp-kafka-reader";

	@Input(AuditExchange.INPUT)
	SubscribableChannel input();

}