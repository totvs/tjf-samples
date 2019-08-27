package com.tjf.sample.github.aggregate.infra;

import javax.validation.constraints.NotNull;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

@Aggregate
public class HabitantModel {

    @AggregateIdentifier
	private String id;

	@NotNull
	private String name;

	@NotNull
	private String gender;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
