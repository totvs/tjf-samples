package com.tjf.sample.github.messaging;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StarShipArrivedEvent {

	public static final transient String NAME = "StarShipArrivedEvent";
	public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";

	private String name;
}
