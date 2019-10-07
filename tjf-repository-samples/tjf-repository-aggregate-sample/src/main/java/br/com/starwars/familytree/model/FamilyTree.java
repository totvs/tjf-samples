package br.com.starwars.familytree.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

@Aggregate
public class FamilyTree {

	@AggregateIdentifier
	private String id;
	private Person person;
	private List<Relative> relatives;

	public FamilyTree(Person person) {
		this(person, new ArrayList<Relative>());
	}

	public FamilyTree(Person person, List<Relative> relatives) {
		this.id = UUID.randomUUID().toString();
		this.person = person;
		this.relatives = relatives;
	}

	public void addRelative(Relative relative) {
		this.relatives.add(relative);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
