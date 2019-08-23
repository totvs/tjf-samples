package br.com.starwars.familytree.enums;

public enum Relationship {

	GRANDFATHER("grandfather"), GRANDMOTHER("grandmother"), FATHER("parent"), MOTHER("mother"), GODFATHER("godfather"),
	HUSBAND("husband"), WIFE("wife"), SON("son"), DAUGHTER("daughter"), GRANDSON("grandson"),
	GRANDDAUGHTER("granddaughter");

	private final String relationship;

	private Relationship(String relationship) {
		this.relationship = relationship;
	}

	public String getRelationship() {
		return relationship;
	}

}
