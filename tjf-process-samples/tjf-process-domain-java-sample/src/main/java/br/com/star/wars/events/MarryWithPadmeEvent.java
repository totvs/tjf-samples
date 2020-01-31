package br.com.star.wars.events;

import br.com.star.wars.commands.MarryWithPadmeCommand;

public class MarryWithPadmeEvent {

	private boolean killSandPeople;
	private boolean killMaceWindu;
	
	public MarryWithPadmeEvent() {
	}
	
	public boolean isKillSandPeople() {
		return killSandPeople;
	}

	public void setKillSandPeople(boolean killSandPeople) {
		this.killSandPeople = killSandPeople;
	}

	public boolean isKillMaceWindu() {
		return killMaceWindu;
	}

	public void setKillMaceWindu(boolean killMaceWindu) {
		this.killMaceWindu = killMaceWindu;
	}

	public static MarryWithPadmeEvent from (MarryWithPadmeCommand command) {
		MarryWithPadmeEvent event = new MarryWithPadmeEvent();
		return event;
	}
	
}