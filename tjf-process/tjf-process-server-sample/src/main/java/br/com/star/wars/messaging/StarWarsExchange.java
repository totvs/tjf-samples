package br.com.star.wars.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface StarWarsExchange {

	String ROUTING_KEY = "type";
	
	String INPUT = "star-wars-input";
	
	@Input(StarWarsExchange.INPUT)
	SubscribableChannel input();

	String OUTPUT = "star-wars-output";

	@Output(StarWarsExchange.OUTPUT)
	MessageChannel output();
	
}
