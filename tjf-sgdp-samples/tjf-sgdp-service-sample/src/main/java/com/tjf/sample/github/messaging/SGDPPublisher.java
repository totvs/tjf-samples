package com.tjf.sample.github.messaging;

import org.springframework.cloud.stream.annotation.EnableBinding;

import com.totvs.sgdp.sdk.services.SGDPRabbitExchange;
import com.totvs.sgdp.sdk.services.data.SGDPDataCommand;
import com.totvs.sgdp.sdk.services.mask.SGDPMaskCommand;
import com.totvs.tjf.messaging.context.TOTVSMessage;
import com.totvs.tjf.messaging.context.TOTVSMessageBuilder;

@EnableBinding(SGDPRabbitExchange.class)
public class SGDPPublisher {

	private SGDPRabbitExchange exchange;

	public SGDPPublisher(SGDPRabbitExchange exchange) {
		this.exchange = exchange;
	}

	public void publish(SGDPDataCommand command) {
		var totvsMessage = TOTVSMessageBuilder.withType("SGDPDataCommand").setContent(command).build();
		totvsMessage.sendTo(exchange.outputChannel());
	}


	public void publish(SGDPMaskCommand command) {
		var message = TOTVSMessageBuilder.withType("SGDPMaskCommand").setContent(command).build();
		message.sendTo(exchange.outputChannel());
	}

}
