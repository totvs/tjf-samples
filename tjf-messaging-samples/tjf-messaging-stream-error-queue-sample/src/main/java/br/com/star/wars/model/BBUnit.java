package br.com.star.wars.model;

import javax.validation.constraints.NotBlank;

public class BBUnit {

	@NotBlank
	private String name;

	private String partner;

	@NotBlank
	private String mission;

	@NotBlank
	private String message;

	public BBUnit() {

	}

	public BBUnit(String name, String partner, String mission, String message) {
		this.name = name;
		this.partner = partner;
		this.mission = mission;
		this.message = message;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getMission() {
		return mission;
	}

	public void setMission(String mission) {
		this.mission = mission;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
