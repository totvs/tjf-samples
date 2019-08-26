package br.com.star.wars.messaging.events;

public class StarShipArrivedEvent {

	public static final transient String NAME = "StarShipArrivedEvent";
	public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";
 
	private String name;

	public StarShipArrivedEvent() {
	}
	
	public StarShipArrivedEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
