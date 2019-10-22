package br.com.star.wars.messaging.events;

public class StarShipArrivedWithoutTenantEvent {

	public static final transient String NAME = "StarShipArrivedWithoutTenantEvent";
	public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";
 
	private String name;

	public StarShipArrivedWithoutTenantEvent() {
	}
	
	public StarShipArrivedWithoutTenantEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
