package com.tjf.sample.github.multidb.entity;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
	@GeneratedValue(strategy = GenerationType.UUID)

	private String id;

	@NotNull
	private String name;

	@NotNull
	private String gender;

}