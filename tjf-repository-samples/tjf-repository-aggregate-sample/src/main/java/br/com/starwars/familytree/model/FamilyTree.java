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
	private List<Relative> relatives = new ArrayList<>();

	public FamilyTree() {
		this.id = UUID.randomUUID().toString();
	}

	public FamilyTree(Person person) {
		this.id = UUID.randomUUID().toString();
		this.person = person;
	}

	public FamilyTree(Person person, List<Relative> relatives) {
		this.id = UUID.randomUUID().toString();
		this.person = person;
		this.relatives = relatives;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public List<Relative> getRelatives() {
		return this.relatives;
	}

	public void setRelatives(List<Relative> relatives) {
		this.relatives = relatives;
	}

	public void addRelative(Relative relative) {
		this.relatives.add(relative);
	}

}
