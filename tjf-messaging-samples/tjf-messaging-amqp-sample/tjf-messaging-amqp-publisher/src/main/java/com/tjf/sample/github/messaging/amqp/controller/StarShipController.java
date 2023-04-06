package com.tjf.sample.github.messaging.amqp.controller;

import com.tjf.sample.github.messaging.amqp.events.StarShipArrivedEvent;
import com.tjf.sample.github.messaging.amqp.infrastructure.messaging.StarShipPublisher;
import com.totvs.tjf.messaging.context.CloudEventsInfo;
import com.totvs.tjf.messaging.context.TransactionInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/starship")
public class StarShipController {

    public static final String APPLICATION_CLOUDEVENTS_JSON = "application/cloudevents+json";

    @Value("${spring.application.name:}")
    private String applicationName;

    private final StarShipPublisher publisher;

    @GetMapping("/arrived")
    String starShipArrived(@RequestParam("name") String name) {
        log.info("Starship arrived name: {}",  name);
        StarShipArrivedEvent starShipArrivedEvent = new StarShipArrivedEvent(name);
        publisher.publishEvent(starShipArrivedEvent);
        return "The identification of the arrived starship " + name + " was sent!";
    }

    @GetMapping("/arrived-tm")
    String starShipArrivedTm(@RequestParam("name") String name) {
        log.info("Starship arrived name: {}",  name);
        StarShipArrivedEvent starShipArrivedEvent = new StarShipArrivedEvent(name);
        publisher.publishEvent(starShipArrivedEvent, "starShipArrivedEvent");
        return "The identification of the arrived starship " + name + " was sent as totvs message!";
    }

    @GetMapping("/arrived-tm-ce")
    String starShipArrivedTmCe(@RequestParam("name") String name) {
        log.info("Starship arrived name: {}",  name);
        StarShipArrivedEvent starShipArrivedEvent = new StarShipArrivedEvent(name);
        String id = UUID.randomUUID().toString();
        String correlationId = UUID.randomUUID().toString();
        String taskId = UUID.randomUUID().toString();
        TransactionInfo transaction = new TransactionInfo(id, applicationName);
        CloudEventsInfo cloudEventsInfo = new CloudEventsInfo(taskId, name, "tenant", correlationId, APPLICATION_CLOUDEVENTS_JSON);
        publisher.publishEvent(starShipArrivedEvent, "starShipArrivedEvent", transaction, cloudEventsInfo);
        return "The identification of the arrived starship " + name + " was sent as totvs message as cloud event!";
    }
}
