package com.tjf.sample.github.tenantschema.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

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
