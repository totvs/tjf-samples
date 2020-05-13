package com.tjf.sample.github.repositoryaggregate.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.totvs.tjf.core.stereotype.Aggregate;
import com.totvs.tjf.core.stereotype.AggregateIdentifier;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
		this();
		this.person = person;
	}

	public FamilyTree(Person person, List<Relative> relatives) {
		this();
		this.person = person;
		this.relatives = relatives;
	}

	public void addRelative(Relative relative) {
		this.relatives.add(relative);
	}

}
