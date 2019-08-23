package br.com.starwars.familytree.model;

import java.util.ArrayList;
import java.util.List;

import com.totvs.tjf.core.stereotype.Aggregate;

import br.com.starwars.familytree.enums.Gender;

@Aggregate
public class FamilyTree extends Person {

	private List<Relative> relatives = new ArrayList<>();

	public FamilyTree(String id, String name, Gender gender) {
		super(id, name, gender);
	}

	public FamilyTree(String id, String name, Gender gender, List<Relative> relatives) {
		super(id, name, gender);
		this.relatives = relatives;
	}

	public void addRelative(Relative relative) {
		this.relatives.add(relative);
	}

}
