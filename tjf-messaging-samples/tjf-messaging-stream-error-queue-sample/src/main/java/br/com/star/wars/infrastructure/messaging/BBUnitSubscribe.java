package br.com.star.wars.infrastructure.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.core.validation.ValidatorService;
import com.totvs.tjf.messaging.WithoutTenant;

import br.com.star.wars.event.BBUnitSendMission;
import br.com.star.wars.exceptions.BBUnitException;
import br.com.star.wars.model.BBUnit;

@EnableBinding(BBUnitExchange.class)
public class BBUnitSubscribe {

	@Autowired
	private ValidatorService validator;

	private static int contError;

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