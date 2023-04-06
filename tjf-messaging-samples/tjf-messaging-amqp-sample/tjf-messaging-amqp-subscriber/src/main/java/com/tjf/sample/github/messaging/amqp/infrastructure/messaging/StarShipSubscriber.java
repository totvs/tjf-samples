package com.tjf.sample.github.messaging.amqp.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.messaging.amqp.events.StarShipArrivedEvent;
import com.totvs.tjf.messaging.context.AmqpTOTVSMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class StarShipSubscriber {

    private final ObjectMapper objectMapper;

    public static final String QUEUE_1 = "starship-queue-1";
    public static final String QUEUE_2 = "starship-queue-2";
    public static final String QUEUE_3 = "starship-queue-3";

    @RabbitListener(queues = QUEUE_1)
    public void receive(@Payload StarShipArrivedEvent starShipArrivedEvent) {
        log.info("Received message from queue: {}", QUEUE_1);
        log.info("StarShipArrivedEvent with name {}", starShipArrivedEvent.getName());
    }

    @SneakyThrows
    @RabbitListener(queues = QUEUE_2)
    public void receiveTotvsMessage(@Payload AmqpTOTVSMessage<StarShipArrivedEvent> message) {
        log.info("Received message from queue {}, message: {}", QUEUE_2, objectMapper.writeValueAsString(message));
        log.info(objectMapper.writeValueAsString(message));
    }

    @SneakyThrows
    @RabbitListener(queues = QUEUE_3)
    public void receiveTotvsMessageAsCloudEvent(@Payload AmqpTOTVSMessage<StarShipArrivedEvent> message) {
        log.info("Received message from queue {}, message: {}", QUEUE_3, objectMapper.writeValueAsString(message));
    }
}
