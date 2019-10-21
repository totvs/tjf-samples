package br.com.star.wars.messaging.events;

import javax.validation.constraints.NotBlank;

public class StarShipLeftEvent {

	public static final transient String NAME = "StarShipLeftEvent";
	public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";

	@NotBlank
	private String name;

	public StarShipLeftEvent() {
	}

	public StarShipLeftEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
