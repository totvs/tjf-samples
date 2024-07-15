package com.tjf.sample.github.metrics.lscloud.error;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.totvs.tjf.lscloud.message.LogLicenseMessage;
import com.totvs.tjf.messaging.context.TOTVSMessage;

@Configuration
public class ErrorSubscriber {

	private static final Logger LOG = LoggerFactory.getLogger(ErrorSubscriber.class);
	private List<LogLicenseMessage> messages = new ArrayList<>();
	
	@Bean(name = "com.totvs.tjf.lscloud.message.LogLicense")
    public Consumer<TOTVSMessage<LogLicenseMessage>> ErrorMessage() {
        return message -> {
        	messages.add(message.getContent());
        	
            LOG.info("Lscloud error received:\nType: {}\nContent: {}", 
                    message.getHeader().getType(),
                    message.getContent());};

    }
	
	public Optional<LogLicenseMessage> getLog() {
		return messages.size() > 0 ? Optional.of(messages.remove(0)) : Optional.empty();
	}
}
