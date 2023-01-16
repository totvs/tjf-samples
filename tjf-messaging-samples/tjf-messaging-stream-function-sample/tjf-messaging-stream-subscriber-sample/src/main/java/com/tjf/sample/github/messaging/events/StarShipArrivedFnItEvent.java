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

	public static final transient String NAME = "StarShipArrivedItEvent";
	public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";
	public static final transient String CONDITIONAL_EXPRESSION_CLOUDEVENT = "headers['type']=='" + NAME + "CloudEvent'";

	private String name;
}
