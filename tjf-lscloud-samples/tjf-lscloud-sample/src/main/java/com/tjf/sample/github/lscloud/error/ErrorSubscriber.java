package com.tjf.sample.github.lscloud.error;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.totvs.tjf.messaging.context.TOTVSMessage;

@Component
public class ErrorSubscriber {

	private static final Logger LOG = LoggerFactory.getLogger(ErrorSubscriber.class);

	@Bean(name = "com.totvs.tjf.lscloud.message.LogLicense")
	public Consumer<TOTVSMessage<?>> ErrorMessage() {
		return message -> {
			LOG.info("Lscloud error received:\nType: {}\nContent: {}", 
					message.getHeader().getType(),
					message.getContent());};

	}
	

}
