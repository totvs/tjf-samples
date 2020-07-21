package com.tjf.sample.github.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.sgdp.services.SGDPRabbitExchange;
import com.totvs.tjf.sgdp.services.data.SGDPDataCommand;
import com.totvs.tjf.sgdp.services.mask.SGDPMaskCommand;

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