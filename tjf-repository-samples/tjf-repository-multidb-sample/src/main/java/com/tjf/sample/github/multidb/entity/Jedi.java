package com.tjf.sample.github.multidb.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Jedi {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private String id;

	@NotNull
	private String name;

	@NotNull
	private String gender;

}
