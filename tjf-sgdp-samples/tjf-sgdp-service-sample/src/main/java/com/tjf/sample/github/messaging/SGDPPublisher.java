package com.tjf.sample.github.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.totvs.sgdp.sdk.services.SGDPRabbitExchange;
import com.totvs.sgdp.sdk.services.data.SGDPDataCommand;
import com.totvs.sgdp.sdk.services.mask.SGDPMaskCommand;
import com.totvs.tjf.core.message.TOTVSMessage;

@EnableBinding(SGDPRabbitExchange.class)
public class SGDPPublisher {

	private SGDPRabbitExchange exchange;

	public SGDPPublisher(SGDPRabbitExchange exchange) {
		this.exchange = exchange;
	}

	public void publish(SGDPDataCommand command) {
		TOTVSMessage<SGDPDataCommand> message = new TOTVSMessage<SGDPDataCommand>("SGDPDataCommand", command);
		message.sendTo(exchange.outputChannel());
	}

	public void publish(SGDPMaskCommand command) {
		TOTVSMessage<SGDPMaskCommand> message = new TOTVSMessage<SGDPMaskCommand>("SGDPMaskCommand", command);
		message.sendTo(exchange.outputChannel());
	}

}