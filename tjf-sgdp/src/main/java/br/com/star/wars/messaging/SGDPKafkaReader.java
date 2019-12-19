package br.com.star.wars.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface SGDPKafkaReader {
	
	String INPUT = "sgdp-kafka-reader";

	@Input(SGDPKafkaReader.INPUT)
	SubscribableChannel input();
    
}