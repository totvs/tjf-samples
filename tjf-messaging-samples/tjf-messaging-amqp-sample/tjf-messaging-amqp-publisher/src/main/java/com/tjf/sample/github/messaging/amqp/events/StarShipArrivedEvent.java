package com.tjf.sample.github.messaging.amqp.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StarShipArrivedEvent {

    private String name;
}
