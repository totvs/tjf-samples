package br.com.star.wars.messaging.events;

public class StarShipLeftEvent {

	public static final transient String NAME = "StarShipLeftEvent";
	public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";
 
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
