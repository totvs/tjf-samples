package br.com.starwars.familytree.model;

import br.com.starwars.familytree.enums.Gender;
import br.com.starwars.familytree.enums.Relationship;

public class Relative extends Person {

	private final Relationship relationship;

	public Relative(String id, String name, Gender gender, Relationship relationship) {
		super(id, name, gender);
		this.relationship = relationship;
	}

	public Relationship getRelationship() {
		return relationship;
	}

}
