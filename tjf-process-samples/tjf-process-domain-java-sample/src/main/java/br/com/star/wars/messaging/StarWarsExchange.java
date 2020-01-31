package br.com.star.wars.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface StarWarsExchange {

	String ROUTING_KEY = "type";
	
	String OUTPUT = "star-wars-output";

	@Output(StarWarsExchange.OUTPUT)
	MessageChannel output();
	
}
