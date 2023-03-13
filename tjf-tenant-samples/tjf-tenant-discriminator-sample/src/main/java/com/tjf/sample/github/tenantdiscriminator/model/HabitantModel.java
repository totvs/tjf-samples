package com.tjf.sample.github.tenantdiscriminator.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "habitant")
public class HabitantModel {

	@EmbeddedId
	private HabitantModelId id;

	@NotNull
	private String name;

	@NotNull
	private String gender;

}
