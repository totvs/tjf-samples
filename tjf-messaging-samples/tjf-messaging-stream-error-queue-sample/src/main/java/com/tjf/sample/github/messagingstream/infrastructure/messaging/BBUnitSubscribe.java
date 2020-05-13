package com.tjf.sample.github.messagingstream.infrastructure.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.tjf.sample.github.messagingstream.event.BBUnitSendMission;
import com.tjf.sample.github.messagingstream.exceptions.BBUnitException;
import com.tjf.sample.github.messagingstream.model.BBUnit;
import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.core.validation.ValidatorService;
import com.totvs.tjf.messaging.WithoutTenant;

@EnableBinding(BBUnitExchange.class)
public class BBUnitSubscribe {

	@Autowired
	private ValidatorService validator;

	private static int contError = 0;

	@WithoutTenant
	@StreamListener(target = BBUnitExchange.INPUT, condition = BBUnitSendMission.CONDITIONAL_EXPRESSION)
	public void subscribeMessageMission(TOTVSMessage<BBUnit> message) {
		if (contError == 0) {
			validator.validate(message.getContent()).ifPresent(violations -> {
				contError = 1;
				throw new BBUnitException(violations);
			});
		}
	}

}