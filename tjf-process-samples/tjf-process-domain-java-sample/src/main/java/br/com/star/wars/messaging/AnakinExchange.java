package br.com.star.wars.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface AnakinExchange {

	String ROUTING_KEY = "type";
	
	String INPUT = "anakin-input";
	
	@Input(AnakinExchange.INPUT)
	SubscribableChannel input();

	String OUTPUT = "anakin-output";

	@Output(AnakinExchange.OUTPUT)
	MessageChannel output();
	
}