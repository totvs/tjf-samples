package com.tjf.sample.github.repositoryaggregate.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Relative extends Person {

	private String relationship;

	public Relative(String id, String name, String gender, String relationship) {
		setId(id);
		setName(name);
		setGender(gender);
		setRelationship(relationship);
	}

}
