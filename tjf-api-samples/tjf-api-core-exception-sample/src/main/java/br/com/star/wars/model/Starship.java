package br.com.star.wars.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class Starship {

	@NotBlank(message = "{Starship.name.NotBlank}")
	private String name;

	@NotBlank(message = "{Starship.description.NotBlank}")
	@Size(min = 1, max = 15, message = "{Starship.description.Size}")
	private String description;

	@Max(value = 5, message = "{Startshp.crew.Max}")
	private int crew;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCrew() {
		return crew;
	}

	public void setCrew(int crew) {
		this.crew = crew;
	}

}
