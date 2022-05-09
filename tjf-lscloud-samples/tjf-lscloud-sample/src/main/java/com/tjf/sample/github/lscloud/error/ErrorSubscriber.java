package com.tjf.sample.github.lscloud.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.messaging.context.TOTVSMessage;
import com.totvs.tjf.lscloud.messaging.LscloudExchange;
import com.totvs.tjf.messaging.WithoutTenant;

import static com.totvs.tjf.lscloud.messaging.LscloudExchangeChannel.Channel.INPUT;

@EnableBinding(LscloudExchange.class)
public class ErrorSubscriber {

	private static final Logger LOG = LoggerFactory.getLogger(ErrorSubscriber.class);

	@StreamListener(value = INPUT)
	@WithoutTenant(ignore = true)
	public void error(TOTVSMessage<?> message) {
		LOG.info("Lscloud error received:\nType: {}\nContent: {}", 
				message.getHeader().getType(),
				message.getContent());
	}
}
