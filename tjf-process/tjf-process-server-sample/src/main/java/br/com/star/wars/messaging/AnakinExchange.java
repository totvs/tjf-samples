package br.com.star.wars.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface AnakinExchange {

	String ROUTING_KEY = "type";
	
	String OUTPUT = "anakin-output";

	@Output(AnakinExchange.OUTPUT)
	MessageChannel output();
	
}