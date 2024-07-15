package com.tjf.sample.github.messaging.amqp.infrastructure.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.tjf.sample.github.messaging.amqp.events.StarShipArrivedEvent;
import com.totvs.tjf.messaging.context.CloudEventsInfo;
import com.totvs.tjf.messaging.context.TOTVSMessageBuilder;
import com.totvs.tjf.messaging.context.TransactionInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StarShipPublisher {

    private final RabbitTemplate rabbitTemplate;

    public static final String EXCHANGE = "starship-exchange-1";
    public static final String ROUTING_KEY = "starship-routing-key-1";

    public static final String EXCHANGE_2 = "starship-exchange-2";
    public static final String ROUTING_KEY_2 = "starship-routing-key-2";

    public static final String EXCHANGE_3 = "starship-exchange-3";
    public static final String ROUTING_KEY_3 = "starship-routing-key-3";

    public void publishEvent(StarShipArrivedEvent starShipArrivedEvent) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, starShipArrivedEvent);
    }

    public <T> void publishEvent(T event, String eventName) {
        TOTVSMessageBuilder.asAmqp().withType(eventName).setContent(event).setTenantId("testTenant1").build()
                .sendTo(rabbitTemplate, EXCHANGE_2, ROUTING_KEY_2);
    }

    public <T> void publishEvent(T event, String eventName, TransactionInfo transactionInfo, CloudEventsInfo cloudEventsInfo) {
    	TOTVSMessageBuilder.asAmqp().withType(eventName).setContent(event)
                .setTransactionInfo(transactionInfo)
                .setCloudEventsInfo(cloudEventsInfo)
                .setTenantId("testTenant2")
                .build()
                .sendTo(rabbitTemplate, EXCHANGE_3, ROUTING_KEY_3);
    }
}
