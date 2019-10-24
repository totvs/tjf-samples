package br.com.star.wars.event;

import br.com.star.wars.model.BBUnit;

public class BBUnitSendMission {

	public static final String MISSION = "BBUnitSendMission";
	public static final String CONDITIONAL_EXPRESSION = "headers['type']=='" + MISSION + "'";

	private BBUnit bbUnit;

	public BBUnitSendMission() {
	}

	public BBUnitSendMission(BBUnit bbUnit) {
		this.bbUnit = bbUnit;
	}

	public BBUnit getBbUnit() {
		return bbUnit;
	}

	public void setBbUnit(BBUnit bbUnit) {
		this.bbUnit = bbUnit;
	}

}
