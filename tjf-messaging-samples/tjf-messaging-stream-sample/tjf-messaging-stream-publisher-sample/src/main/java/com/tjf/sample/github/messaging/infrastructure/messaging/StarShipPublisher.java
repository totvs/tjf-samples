package com.tjf.sample.github.messaging.infrastructure.messaging;

import java.net.URI;
import java.util.UUID;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.totvs.tjf.core.message.TOTVSMessage;
import com.totvs.tjf.core.message.TransactionInfo;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.builder.CloudEventBuilder;
import io.cloudevents.jackson.JsonCloudEventData;

@EnableBinding(StarShipExchange.class)
public class StarShipPublisher {

	StarShipExchange exchange;
	ObjectMapper mapper;

	public StarShipPublisher(StarShipExchange exchange, ObjectMapper mapper) {
		this.exchange = exchange;
		this.mapper = mapper;
	}

	public <T> void publish(T event, String eventName) {

		new TOTVSMessage<T>(eventName, event).sendTo(exchange.output());
	}

	public <T> void publish(T event, String eventName, TransactionInfo transactionInfo) {

		new TOTVSMessage<T>(eventName, event, transactionInfo).sendTo(exchange.output());
	}

	public <T> void publishCloudEvent(T event, String eventName) {

		var data = JsonCloudEventData.wrap(mapper.valueToTree(event));

		var messageId = UUID.randomUUID().toString();

		var cloudEvent = CloudEventBuilder.v1()
				.withId(messageId)
				.withType(eventName)
				.withSource(URI.create(messageId))
				.withData(data)
				.build();

		// var rr = GenericStructuredMessageReader.from(cloudEvent, )

		//var payload = EventFormatProvider.getInstance().resolveFormat("application/cloudevents+json").serialize(cloudEvent);

		//var msg = MessageBuilder.withPayload(payload).setHeader("type", eventName).setHeader("contentType", "application/cloudevents+json").build();

		// exchange.output().send(msg);

		// exchange.output().send(GenericStructuredMessageReader.from(rr,
		// "appication/cloudevent+json"));

		send(exchange.output(), cloudEvent);
	}

	public static void send(MessageChannel chanel, CloudEvent cloudEvent) {

		//var payload = EventFormatProvider.getInstance().resolveFormat("application/cloudevents+json").serialize(cloudEvent);

		//var message = MessageBuilder.withPayload(payload).setHeader("type", cloudEvent.getType())
		//		.setHeader("contentType", "application/cloudevents+json").build();

		//chanel.send(message);
		
		chanel.send(MessageBuilder.withPayload(cloudEvent).build());
	}
}