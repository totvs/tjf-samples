package com.tjf.sample.github.messaging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.cloudevents.spring.messaging.CloudEventMessageConverter;

@Configuration
public class CloudEventMessageConverterConfiguration {
	@Bean
	public CloudEventMessageConverter cloudEventMessageConverter() {
		return new CloudEventMessageConverter();
	}
}
