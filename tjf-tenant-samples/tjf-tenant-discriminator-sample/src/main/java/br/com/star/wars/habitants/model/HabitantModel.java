package br.com.star.wars.habitants.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "habitant")
public class HabitantModel {

	@EmbeddedId
	private HabitantModelId id;

	@NotNull
	private String name;

	@NotNull
	private String gender;

	public HabitantModelId getId() {
		return this.id;
	}

	public void setId(HabitantModelId id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
