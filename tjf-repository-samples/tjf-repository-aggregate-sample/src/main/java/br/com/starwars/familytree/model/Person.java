package br.com.starwars.familytree.model;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

@Aggregate
public class Person extends Human {

	@AggregateIdentifier
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Person(String id) {
		super();
		this.id = id;
	}

	public Person() {
		super();
	}

}
