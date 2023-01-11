package com.tjf.sample.github.messaging.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StarShipArrivedFnItEvent {

    public static final transient String NAME = "StarShipArrivedFnItEvent";
    public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";

    private String name;
}
