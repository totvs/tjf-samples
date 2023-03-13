package com.tjf.sample.github.tenantschema.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "habitant")
public class HabitantModel {

	@Id
	private String id;

	@NotNull
	private String name;

	@NotNull
	private String gender;

}
