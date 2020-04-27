package br.com.tjf.events;

public class MessageEvent {

	public static final transient String NAME = "MessageEvent";
	public static final transient String CONDITIONAL_EXPRESSION = "headers['type']=='" + NAME + "'";

	private String name;

	public MessageEvent() {
	}

	public MessageEvent(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
