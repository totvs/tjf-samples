package br.com.star.wars.messaging.infrastructure.messaging;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface StarShipExchange {

	String OUTPUT = "starship-output";

	@Output(StarShipExchange.OUTPUT)
	MessageChannel output();

}