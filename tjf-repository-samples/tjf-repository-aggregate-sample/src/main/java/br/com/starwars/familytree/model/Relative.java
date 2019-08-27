package br.com.starwars.familytree.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Relative extends Person {

	private String relationship;

	public Relative(String id, String name, String gender, String relationship) {
		setId(id);
		setName(name);
		setGender(gender);
		setRelationship(relationship);
	}

}
