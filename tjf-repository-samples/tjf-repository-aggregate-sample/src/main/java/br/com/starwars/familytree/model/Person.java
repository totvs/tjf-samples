package br.com.starwars.familytree.model;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

import br.com.starwars.familytree.enums.Gender;

@Aggregate
public class Person extends Human {

	@AggregateIdentifier
	private final String id;

	public Person(String id, String name, Gender gender) {
		super(name, gender);
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
