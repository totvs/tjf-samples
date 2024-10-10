package com.tjf.sample.github.messaging.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tjf.sample.github.messaging.amqp.events.StarShipArrivedEvent;
import com.totvs.tjf.messaging.context.AmqpTOTVSMessage;
import com.totvs.tjf.mock.test.Semaphore;
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

    private final Semaphore semaphore;

    public static final String QUEUE_1 = "starship-queue-1";
    public static final String QUEUE_2 = "starship-queue-2";
    public static final String QUEUE_3 = "starship-queue-3";

    @RabbitListener(queues = {QUEUE_1})
    @SneakyThrows
    public void receive(@Payload StarShipArrivedEvent starShipArrivedEvent) {
        log.info("Received message from queue: {}", QUEUE_1);
        log.info("StarShipArrivedEvent with name {}", starShipArrivedEvent.getName());
        StarShipControllerIT.starShipArrivedEvent = starShipArrivedEvent;
        Thread.sleep(100);
        semaphore.sendSignal();
    }

    @SneakyThrows
    @RabbitListener(queues = {QUEUE_2})
    public void receiveTotvsMessage(@Payload AmqpTOTVSMessage<StarShipArrivedEvent> message) {
        log.info("Received message from queue {}, message: {}", QUEUE_2, objectMapper.writeValueAsString(message));
        log.info(objectMapper.writeValueAsString(message));
        StarShipControllerIT.amqpTOTVSMessage = message;
        Thread.sleep(100);
        semaphore.sendSignal();
    }

    @SneakyThrows
    @RabbitListener(queues = {QUEUE_3})
    public void receiveTotvsMessageAsCloudEvent(@Payload AmqpTOTVSMessage<StarShipArrivedEvent> message) {
        log.info("Received message from queue {}, message: {}", QUEUE_3, objectMapper.writeValueAsString(message));
        StarShipControllerIT.amqpTOTVSMessage = message;
        Thread.sleep(100);
        semaphore.sendSignal();
    }
}
