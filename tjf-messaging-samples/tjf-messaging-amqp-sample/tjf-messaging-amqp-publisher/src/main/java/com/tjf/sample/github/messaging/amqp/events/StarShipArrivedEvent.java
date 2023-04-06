package com.tjf.sample.github.messaging.amqp.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StarShipArrivedEvent implements Serializable {

    private String name;
}
