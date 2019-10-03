package br.com.starwars.familytree.model;

public class Relative extends Person {

	private String relationship;

	public Relative() {
		super();
	}

	public Relative(String id, String name, String gender, String relationship) {
		setId(id);
		setName(name);
		setGender(gender);
		setRelationship(relationship);
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
}
