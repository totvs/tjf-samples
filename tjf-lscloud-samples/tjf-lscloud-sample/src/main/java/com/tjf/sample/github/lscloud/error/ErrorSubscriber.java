package com.tjf.sample.github.lscloud.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.totvs.tjf.lscloud.messaging.LscloudExchange;
import com.totvs.tjf.messaging.WithoutTenant;

import static com.totvs.tjf.lscloud.messaging.LscloudExchangeChannel.Channel.INPUT;

import io.cloudevents.CloudEvent;

@EnableBinding(LscloudExchange.class)
public class ErrorSubscriber {

	private static final Logger LOG = LoggerFactory.getLogger(ErrorSubscriber.class);

	@StreamListener(value = INPUT)
	@WithoutTenant(ignore = true)
	public void error(CloudEvent message) {
		LOG.info("Lscloud error received:\nId: {}\nType: {}\nData: {}", 
				message.getId(), 
				message.getType(),
				message.getData());
	}
}
