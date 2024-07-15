package com.tjf.sample.github.messagingstream.infrastructure.messaging;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tjf.sample.github.messagingstream.exceptions.BBUnitException;
import com.tjf.sample.github.messagingstream.model.BBUnit;
import com.totvs.tjf.core.validation.ValidatorService;
import com.totvs.tjf.messaging.context.TOTVSMessage;

@Configuration
public class BBUnitSubscribe {

	@Autowired
	private ValidatorService validator;

	private static int contError = 0;

	@Bean
	public Consumer<TOTVSMessage<BBUnit>> subscribeMessageMission() {
		return message -> {
			if (contError == 0) {
				validator.validate(message.getContent()).ifPresent(violations -> {
					contError = 1;
					throw new BBUnitException(violations);
				});
			}
		};
	}

}