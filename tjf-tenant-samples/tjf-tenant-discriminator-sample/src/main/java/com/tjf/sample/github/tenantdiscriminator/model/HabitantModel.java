package com.tjf.sample.github.tenantdiscriminator.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
